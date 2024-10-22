function filterMentors() {
    let input = document.getElementById('mentorSearch').value.toLowerCase();
    let cards = document.getElementsByClassName('mentor-card');
    
    for (let i = 0; i < cards.length; i++) {
        let card = cards[i];
        let tags = card.getAttribute('data-tags').toLowerCase();
        if (tags.includes(input)) {
            card.style.display = 'block';
        } else {
            card.style.display = 'none';
        }
    }
}
