const updateBtn = document.getElementById('update-btn')
const description = document.getElementById('description')
const quantity = document.getElementById('quantity')
const price = document.getElementById('price')
const category = document.getElementById('category')
const descriptionError = document.getElementById('description-error')
const quantityError = document.getElementById('quantity-error')
const priceError = document.getElementById('price-error')
const categoryError = document.getElementById('category-error')

updateBtn.addEventListener('click', (e) => {
    let isValid

    if (description.value.trim() === '') {
        descriptionError.innerHTML = 'Description is required'
        description.value = ''
    } else {
        descriptionError.innerHTML = ''
    }

    if (category.value === '') {
        categoryError.innerHTML = 'Category is required'
    } else {
        categoryError.innerHTML = ''
    }

    if (!RegExp('^[0-9]+([.][0]+)?$').test(quantity.value.trim())) {
        quantityError.innerHTML = 'A unsigned integer is required'
        quantity.value = quantity.value.trim()
    } else {
        quantityError.innerHTML = ''
    }

    if (!RegExp('^([0-9]+[.])?[0-9]+$').test(price.value.trim())) {
        priceError.innerHTML = 'A unsigned real number is required'
        price.value = price.value.trim()
    } else {
        priceError.innerHTML = ''
    }

    if (descriptionError.innerHTML !== '' || categoryError.innerHTML !== '' || quantityError.innerHTML !== '' || priceError.innerHTML !== '') {
        isValid = false;
    } else {
        isValid = true;
    }

    if (!isValid) {
        e.preventDefault()
    }
})