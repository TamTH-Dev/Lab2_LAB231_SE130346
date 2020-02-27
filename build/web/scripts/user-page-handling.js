/* global gapi */

// Validate for Searching
const productName = document.getElementById('productName')
const priceLevels = document.getElementsByClassName('price-level')
const searchBtn = document.getElementById('search-btn')
const searchError = document.getElementById('search-error')

const isPriceLevelsChecked = () => {
    const priceLevelsLength = priceLevels.length

    for (let i = 0; i < priceLevelsLength; i++) {
        if (priceLevels[i].checked) {
            return true
        }
    }

    return false
}

searchBtn.addEventListener('click', (e) => {
    if (productName.value.trim() === '' && !isPriceLevelsChecked()) {
        e.preventDefault()
        searchError.style.display = 'block'
        productName.value = productName.value.trim()
    }
})

// Google Sign-Out
function signOut() {
    const auth2 = gapi.auth2.getAuthInstance()
    auth2.signOut().then(function () {
        console.log('User signed out.')
    });
    window.location = 'Logout'
}

function onLoad() {
    gapi.load('auth2', function () {
        gapi.auth2.init();
    });
}


