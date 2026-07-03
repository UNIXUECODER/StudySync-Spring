// Replaces the React `activeTab` state from page.tsx.
// All tab panels are already rendered server-side by Thymeleaf; this just
// toggles which one is visible, and keeps the sidebar nav in sync.
document.addEventListener("DOMContentLoaded", function () {
  var navItems = document.querySelectorAll("[data-tab]");
  var panels = document.querySelectorAll("[data-tab-panel]");

  function activate(tab) {
    navItems.forEach(function (item) {
      var isActive = item.getAttribute("data-tab") === tab;
      item.classList.toggle("navItemActive", isActive);
    });

    panels.forEach(function (panel) {
      var isActive = panel.getAttribute("data-tab-panel") === tab;
      panel.classList.toggle("tabPanelActive", isActive);
    });
  }

  navItems.forEach(function (item) {
    item.addEventListener("click", function () {
      if (item.disabled) {
        return;
      }
      activate(item.getAttribute("data-tab"));
    });
  });

  var startBtn = document.getElementById("start-now-btn");
  if (startBtn) {
    startBtn.addEventListener("click", function () {
      activate("generate");
    });
  }
});
