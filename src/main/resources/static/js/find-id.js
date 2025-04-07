document.addEventListener('DOMContentLoaded', function() {
    flatpickr("#birthDate", {
        dateFormat: "Y-m-d",
        altInput: true,
        altFormat: "Y년 m월 d일",
    });
});
