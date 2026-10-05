package com.example.userlist

import com.example.network.model.User

// user ne pehle se list dekh li hai
// (Success state), aur ab pull-to-refresh kar raha hai.
/*Lekin ye karte hi purani list (users) gayab ho jayegi screen
se — kyunki Loading state ke paas users field hi nahi hai!
User ko poori list disappear hoti dikhegi,
fir spinner, fir wapas list aayegi. Ye flicker hoga, achhi UX nahi.delay ke sath toh aur dekhega flicker*/

/*    sealed class UiState {
        data class Success(val data: List<User>): UiState()
        data class Error(val message: String): UiState()
        data object Loading: UiState()
    }*/

/*Isse users list wahin ki wahin rehti hai,
bas isLoading = true ho jaata hai — list dikhti rahegi background mein,
upar ek chhota spinner/refresh indicator dikha sakte ho. Smooth UX.*/



/*
data class UiState(
val users:List<User> = emptyList(),
val isLoading:Boolean = false,
val errorMessage:String? = null)

*/
data class UiState(
    val data:List<User> = emptyList(),
    val isLoading:Boolean = false,
    val errorMessage:String? = null
)