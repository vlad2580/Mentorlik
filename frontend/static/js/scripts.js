// Фильтрация менторов по введенному тексту
const cards = Array.from(document.getElementsByClassName('mentor-card')); // Кэшируем список карточек

function filterMentors() {
    const input = document.getElementById('mentorSearch').value.trim().toLowerCase();
    cards.forEach(card => {
        const tags = card.getAttribute('data-tags').toLowerCase();
        card.style.display = tags.includes(input) ? 'block' : 'none';
    });
    console.log(`Фильтрация по: "${input}" выполнена.`);
}

// Обработка боковой панели (открыть/закрыть)
const sidebar = document.getElementById('sidebar');
const toggle = document.getElementById('toggle');

if (sidebar && toggle) {
    toggle.addEventListener('click', () => {
        sidebar.classList.toggle('closed');
        console.log('Боковая панель: состояние изменено.');
    });
} else {
    console.error('Элемент sidebar или toggle не найден.');
}

// Плавный скроллинг к секции mentors
const scrollToMentorsButton = document.getElementById('scrollToMentors');
const mentorsSection = document.getElementById('mentors');

if (scrollToMentorsButton && mentorsSection) {
    scrollToMentorsButton.addEventListener('click', function (e) {
        e.preventDefault(); 
        mentorsSection.scrollIntoView({
            behavior: 'smooth' // Плавный скроллинг
        });
        console.log('Скроллинг к секции mentors выполнен.');
    });
} else {
    console.error('Элемент scrollToMentorsButton или mentorsSection не найден.');
}