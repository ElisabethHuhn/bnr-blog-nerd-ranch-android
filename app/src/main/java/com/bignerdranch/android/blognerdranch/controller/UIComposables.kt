package com.bignerdranch.android.blognerdranch.controller

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bignerdranch.android.blognerdranch.R
import com.bignerdranch.android.blognerdranch.viewmodel.BlogViewModelImpl

/*
 * A ScreenDestination keeps together all the information needed
 * for navigation to/from the screen
 * Every screen has one of these ScreenDestinations defined for it
 * Best reference https://bignerdranch.com/blog/using-the-navigation-component-in-jetpack-compose/
 */
interface ScreenDestination {
    val route: String
    val title: Int
}
object ListDestination : ScreenDestination {
    override val route: String
        get() = "list_screen"
    override val title: Int
        get() = R.string.list_title
}
object DetailDestination : ScreenDestination {
    override val route: String
        get() = "detail_screen"
    override val title: Int
        get() = R.string.detail_title
    const val postIdArg = "postId"
    val routeWithArg: String = "$route/{$postIdArg}"
    val arguments = listOf(navArgument(postIdArg) {type = NavType.StringType})
    fun getNavigationListToDetail(postId: String) = "$route/$postId"
}

/*
 * The NavHost is single source of truth for all screen navigation in the app
 */
@ExperimentalMaterial3Api
@Composable
fun MainNavGraph(
    blogViewModel: BlogViewModelImpl,
){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination =  ListDestination.route
    ){
        composable(ListDestination.route){
            //pass navigation as parameter
            ListScreen(
                screenTitle = ListDestination.title,
                onListSelect = {
                    val toDes = DetailDestination.getNavigationListToDetail(it)
                    navController.navigate(toDes)
                },
                viewModel = blogViewModel,
            )
        }

        composable(
            route = DetailDestination.routeWithArg,
            arguments = DetailDestination.arguments,
        ){ backStackEntry ->
            //pass navigation as parameter
            DetailScreen(
                screenTitle = DetailDestination.title,
                postId = backStackEntry.arguments?.getString(DetailDestination.postIdArg) ?: "",
                onBack = {
                    val toDest = ListDestination.route
                     navController.navigate(toDest)
                },
                viewModel = blogViewModel,
            )
        }
    }
}

fun Modifier.scrollEnabled(
    enabled: Boolean,
) = nestedScroll(
    connection = object : NestedScrollConnection {
        override fun onPreScroll(
            available: Offset,
            source: NestedScrollSource
        ): Offset = if(enabled) Offset.Zero else available
    }
)

@ExperimentalMaterial3Api
@Composable
fun ListScreen(
    screenTitle : Int,
    onListSelect: (postId: String) -> Unit,
    viewModel: BlogViewModelImpl

) {
    val post = viewModel.postFlow.collectAsStateWithLifecycle()
    val postMetas = viewModel.postsFlow.collectAsStateWithLifecycle()


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(screenTitle),
                            fontSize = 30.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
            )
        },
    ) { it
        //actually make the API call
        viewModel.fetchPosts()

        Spacer(modifier = Modifier.height(95.0.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(1f)
                .scrollEnabled(
                    enabled = true, //provide a mutable state boolean here
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(15.0.dp))
                Text(
                    text = stringResource(R.string.nav_instructions),
                    fontSize = 20.sp,
                    modifier = Modifier,
                    fontStyle = FontStyle.Italic
                )
            }

            postMetas.value.size.let { numberOfPosts ->
                items(numberOfPosts) { position ->
                    Spacer(modifier = Modifier.height(20.0.dp))
                    val postMeta = postMetas.value[position]
                    Row (
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.clickable { onListSelect(postMeta.postId.toString())  },
                            text = postMeta.postId.toString()
                        )
                        Text(
                            modifier = Modifier.clickable { onListSelect(postMeta.postId.toString())  },
                            text = postMeta.title ?: stringResource(R.string.no_title_available),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(5.0.dp))
                    Text(
                        modifier = Modifier.clickable { onListSelect(postMeta.postId.toString())  },
                        text = "Author: ${postMeta.author?.name ?: stringResource(R.string.no_author_available)}"
                    )
                    Spacer(modifier = Modifier.height(5.0.dp))
                    Text(
                        modifier = Modifier.clickable { onListSelect(postMeta.postId.toString())  },
                        text = "Summary: ${postMeta.summary ?: stringResource(R.string.no_summary_available)}"
                    )
                    Spacer(modifier = Modifier.height(5.0.dp))
                    Text(
                        modifier = Modifier.clickable { onListSelect(postMeta.postId.toString())  },
                        text = "Pub Date : ${postMeta.publishDate ?: stringResource(R.string.no_pub_date_available)}"
                    )
                    Divider(Modifier.fillMaxWidth(), thickness = 2.dp, color = Color.Black) //above your row
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    screenTitle: Int,
    postId: String,
    onBack: () -> Unit,
    viewModel: BlogViewModelImpl
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(screenTitle),
                            fontSize = 30.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
            )
        }
    ) { it
        val post = viewModel.postFlow.collectAsStateWithLifecycle()

        //actually make the API call
        viewModel.fetchPost(postId.toInt())

        Column(
            modifier = Modifier
                .fillMaxWidth(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(70.0.dp))

            if (postId.toInt() == post.value.id) {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = post.value.id.toString())
                    Text(
                        text = post.value.metadata?.title
                            ?: stringResource(R.string.no_title_available),
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(5.0.dp))
                Text(
                    modifier = Modifier,
                    text = post.value.metadata?.author?.name
                        ?: stringResource(R.string.no_author_available),
                    fontSize = 20.sp,
                )
                Spacer(modifier = Modifier.height(15.0.dp))
                Text(
                    modifier = Modifier,
                    text = post.value.body ?: stringResource(R.string.no_blog_text_available),
                    fontSize = 20.sp,
                )
            } else {
                Spacer(modifier = Modifier.height(5.0.dp))
                Text(
                    modifier = Modifier,
                    text = "Blog Post $postId is Loading",
                    fontSize = 20.sp,
                )
            }

            Spacer(modifier = Modifier.height(5.0.dp))
            Button(
                onClick = {
                    //navigate to List Screen
                    onBack.invoke()
                })
            {
                Text(text = stringResource(id = R.string.return_to_list_button))
            }
        }
    }
}

