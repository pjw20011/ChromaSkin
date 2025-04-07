function selectOption(id) {
    document.getElementById(id).checked = true;
    document.querySelectorAll('.image-item').forEach(item => item.classList.remove('active'));
    document.querySelector(`input#${id}`).parentNode.classList.add('active');
}