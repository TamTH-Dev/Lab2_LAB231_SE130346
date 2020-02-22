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

// Validate for Deleting Many Products
const productSelectings = document.getElementsByClassName('product-selecting')
const productsDeletingBtn = document.getElementById('products-deleting-btn')
const productsDeletingError = document.getElementById('products-deleting-error')

const isProductsChecked = () => {
    const productSelectingsLength = productSelectings.length

    for (let i = 0; i < productSelectingsLength; i++) {
        if (productSelectings[i].checked) {
            return true
        }
    }

    return false
}

productsDeletingBtn.addEventListener('click', (e) => {
    if (!isProductsChecked()) {
        e.preventDefault()
        productsDeletingError.style.display = 'block'
    }
})
