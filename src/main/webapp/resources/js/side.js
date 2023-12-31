document.addEventListener('DOMContentLoaded', function () {
    const sidebar = document.getElementById('sidebar');
    const footer = document.querySelector('.footer');
    const sidebarNav = document.querySelector('#sidebar nav');

    window.addEventListener('scroll', function () {
        const sidebarRect = sidebar.getBoundingClientRect();
        const footerRect = footer.getBoundingClientRect();

        if (sidebarRect.bottom >= footerRect.top) {
            // Calculate the distance to move the sidebar up
            const moveDistance = sidebarRect.bottom - footerRect.top;
            sidebar.style.bottom = moveDistance + 'px';
            sidebar.style.border = 0;
            sidebar.style.zIndex = 1;
            sidebar.style.backgroundColor = '#988fb200';
            
            // Adjust the position of the nav inside the sidebar
            sidebarNav.style.position = 'relative';
            sidebarNav.style.top = -moveDistance + 'px';
        } else {
            // Reset the sidebar position when it's not overlapping the footer
            sidebar.style.zIndex = 1;
            sidebar.style.backgroundColor = '#988fb217';
            sidebar.style.bottom = '0';
            sidebar.style.border = '1px solid #bbb1d6';
            sidebarNav.style.position = 'absolute';
            sidebarNav.style.top = '0px'; // You can adjust this value
        }
    });
});