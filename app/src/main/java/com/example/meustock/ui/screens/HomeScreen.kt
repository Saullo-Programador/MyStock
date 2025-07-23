package com.example.meustock.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.meustock.ui.components.FloatingButton
import com.example.meustock.ui.components.SearchComponents
import com.example.meustock.R
import com.example.meustock.ui.components.ItemProduct
import com.example.meustock.ui.viewModel.ListProductViewModel
import com.example.meustock.ui.viewModel.RegisterProductFormViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun HomeScreen(
){
    HomeContent()
}


@Composable
fun HomeContent(){
    //val expanded = remember { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            /*
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                SearchComponents(
                    query = "",
                    onQueryChange = { },
                    onSearch = { },
                    searchResults = listOf(),
                    onResultClick = { },
                    leadingIcon = painterResource(R.drawable.icon_search),
                    placeholder = "",
                    expanded = expanded.value,
                    onExpandedChange = { expanded.value = it },
                    modifier = Modifier.fillMaxWidth(0.9f)
                )
            }
             */
        }
    ) {  innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ListProduct()
            }
        }
    }
}

@Composable
fun ListProduct(
    viewModel: ListProductViewModel = hiltViewModel()
){
    val products by viewModel.product.collectAsState()
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        items(products.size){ index ->
            val product = products[index]
            ItemProduct(
                nameProduct = product.name,
                quantity = product.currentStock,
                price = product.sellingPrice
            )
        }
    }
}

