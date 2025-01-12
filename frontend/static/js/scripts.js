// Filtering mentors based on the entered text
const cards = Array.from(document.getElementsByClassName('mentor-card')); // Cache the list of mentor cards

function filterMentors() {
    const input = document.getElementById('mentorSearch').value.trim().toLowerCase();
    cards.forEach(card => {
        const tags = card.getAttribute('data-tags').toLowerCase();
        card.style.display = tags.includes(input) ? 'block' : 'none';
    });
    console.log(`Filtering by: "${input}" completed.`);
}

// Handling the sidebar (open/close toggle functionality)
const sidebar = document.getElementById('sidebar');
const toggle = document.getElementById('toggle');

if (sidebar && toggle) {
    toggle.addEventListener('click', () => {
        sidebar.classList.toggle('closed');
        console.log('Sidebar state changed.');
    });
} else {
    console.error('Sidebar or toggle element not found.');
}

// Smooth scrolling to the mentors section
const scrollToMentorsButton = document.getElementById('scrollToMentors');
const mentorsSection = document.getElementById('mentors');

if (scrollToMentorsButton && mentorsSection) {
    scrollToMentorsButton.addEventListener('click', function (e) {
        e.preventDefault(); 
        mentorsSection.scrollIntoView({
            behavior: 'smooth' // Smooth scrolling animation
        });
        console.log('Scrolling to the mentors section completed.');
    });
} else {
    console.error('ScrollToMentorsButton or mentorsSection element not found.');
}