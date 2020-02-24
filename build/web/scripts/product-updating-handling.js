const updateBtn = document.getElementById('update-btn')
const deleteBtn = document.getElementById('delete-btn')
const restoreBtn = document.getElementById('restore-btn')
const description = document.getElementById('description')
const quantity = document.getElementById('quantity')
const price = document.getElementById('price')
const category = document.getElementById('category')
const descriptionError = document.getElementById('description-error')
const quantityError = document.getElementById('quantity-error')
const priceError = document.getElementById('price-error')
const categoryError = document.getElementById('category-error')

if (updateBtn) {
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
        } else {
            const doesUpdates = confirm('Are you sure you want to update?')
            if (!doesUpdates) e.preventDefault()
        }
    })
}


if (deleteBtn) {
    deleteBtn.addEventListener('click', (e) => {
        const doesDeletes = confirm('Are you sure you want to delete?')
        if (!doesDeletes) e.preventDefault()
    })
}

if (restoreBtn) {
    restoreBtn.addEventListener('click', (e) => {
        const doesRestores = confirm('Are you sure you want to restore?')
        if (!doesRestores) e.preventDefault()
    })
}