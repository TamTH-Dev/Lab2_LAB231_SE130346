/* global moment */

// Toggle payment history detail
const paymentHistoryItem = document.getElementsByClassName("payment-history-item")
const paymentHistoryDetailContainer = document.getElementsByClassName("payment-history-detail-container")
const length = paymentHistoryItem.length

for (let i = 0; i < length; i++) {
    paymentHistoryItem[i].addEventListener('click', () => {
        if (paymentHistoryDetailContainer[i].style.display === 'none' || paymentHistoryDetailContainer[i].style.display == '') {
            paymentHistoryDetailContainer[i].style.display = 'flex'
        } else {
            paymentHistoryDetailContainer[i].style.display = 'none'
        }
    })
}

// Validate for Searching
const productName = document.getElementById('productName')
const searchedStartingShoppingTime = document.getElementById('searchedStartingShoppingTime')
const searchedEndingShoppingTime = document.getElementById('searchedEndingShoppingTime')
const searchBtn = document.getElementById('search-btn')
const filloutSearchError = document.getElementById('fillout-search-error')
const validSearchError = document.getElementById('valid-search-error')

searchBtn.addEventListener('click', (e) => {
    let isFillOuted = true
    if (productName.value.trim() === '') {
        if (searchedStartingShoppingTime.value.trim() === '' || searchedEndingShoppingTime.value.trim() === '') {
            isFillOuted = false
        }
    } else if (productName.value.trim() !== '') {
        if (searchedStartingShoppingTime.value.trim() === '' || searchedEndingShoppingTime.value.trim() === '') {
            isFillOuted = false
        }
        if (searchedStartingShoppingTime.value.trim() === '' && searchedEndingShoppingTime.value.trim() === '') {
            isFillOuted = true
        }
    }

    if (isFillOuted) {
        if (searchedEndingShoppingTime.value !== '' && searchedEndingShoppingTime !== '') {
            if (!moment(searchedStartingShoppingTime.value, 'DD-MM-YYYY', true).isValid() ||
                    !moment(searchedEndingShoppingTime.value, 'DD-MM-YYYY', true).isValid()) {
                e.preventDefault()
                validSearchError.style.display = 'block'
            }
        }
        filloutSearchError.style.display = 'none'
        productName.value = productName.value.trim()
        searchedStartingShoppingTime.value = searchedStartingShoppingTime.value.trim()
        searchedEndingShoppingTime.value = searchedEndingShoppingTime.value.trim()
    } else {
        e.preventDefault()
        filloutSearchError.style.display = 'block'
        productName.value = productName.value.trim()
        searchedStartingShoppingTime.value = searchedStartingShoppingTime.value.trim()
        searchedEndingShoppingTime.value = searchedEndingShoppingTime.value.trim()
    }
})