const productName = document.getElementById('productName')
const priceLevels = document.getElementsByClassName('price-level')
const searchBtn = document.getElementById('search-btn')
const searchError = document.getElementById('search-error')

const isPriceLevelsChecked = () => {
    let isChecked = false
    const priceLevelsLength = priceLevels.length

    for (let i = 0; i < priceLevelsLength; i++) {
        if (priceLevels[i].checked) {
            isChecked = true
        }
    }

    return isChecked
}

searchBtn.addEventListener('click', (e) => {
    if (productName.value.trim() === '' && !isPriceLevelsChecked()) {
        e.preventDefault()
        searchError.style.display = 'block'
    }
})