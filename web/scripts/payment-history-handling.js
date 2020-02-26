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
const searchError = document.getElementById('search-error')

searchBtn.addEventListener('click', (e) => {
    if (productName.value.trim() === '' && (searchedStartingShoppingTime.value.trim() === '' || searchedEndingShoppingTime.value.trim() === '')) {
        e.preventDefault()
        searchError.style.display = 'block'
        productName.value = productName.value.trim()
        searchedStartingShoppingTime.value = searchedStartingShoppingTime.value.trim()
        searchedEndingShoppingTime.value = searchedEndingShoppingTime.value.trim()
    }
})