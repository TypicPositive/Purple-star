$(document).ready(function() {
  $(".product_slider").owlCarousel({
    items: 4, // Number of products per slide
    loop: true, // Infinite loop
    margin: 10, // Space between items
    nav: true, // Show next/prev arrows
    navText: ['<i class="fa fa-chevron-left"></i>', '<i class="fa fa-chevron-right"></i>'], // Custom navigation icons
    responsive: {
      0: {
        items: 1
      },
      600: {
        items: 2
      },
      1000: {
        items: 4
      }
    }
  });
});
