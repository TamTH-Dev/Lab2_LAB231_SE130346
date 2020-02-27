/* global gapi */

const increaseAmountBtn = document.getElementById('increase-amount-btn')
const descreaseAmountBtn = document.getElementById('descrease-amount-btn')
const productAmount = document.getElementById('product-amount')

increaseAmountBtn.addEventListener('click', () => {
    productAmount.value = parseInt(productAmount.value) + 1
})

descreaseAmountBtn.addEventListener('click', () => {
    if (parseInt(productAmount.value) > 1) {
        productAmount.value = parseInt(productAmount.value) - 1
    }
})

productAmount.addEventListener('change', () => {
    const productAmountValue = productAmount.value
    if (!RegExp('^[0-9]+([.][0]+)?$').test(productAmountValue) || productAmountValue == 0) {
        productAmount.value = 1
    } else {
        productAmount.value = parseInt(productAmountValue)
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