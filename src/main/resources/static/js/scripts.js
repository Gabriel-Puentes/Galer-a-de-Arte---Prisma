/*!
* Start Bootstrap - Grayscale v7.0.6 (https://startbootstrap.com/theme/grayscale)
* Copyright 2013-2023 Start Bootstrap
* Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-grayscale/blob/master/LICENSE)
*/
//
// Scripts
// 

// Espera a que el HTML esté completamente cargado antes de ejecutar el código
window.addEventListener('DOMContentLoaded', event => {

    //reduce el tamaño del navbar cuando el usuario hace scroll
    var navbarShrink = function () {
        const navbarCollapsible = document.body.querySelector('#mainNav');
        if (!navbarCollapsible) {
            return;
        }
        //parte superior: navbar mas grande
        if (window.scrollY === 0) {
            navbarCollapsible.classList.remove('navbar-shrink')
        } else {
            //si se hizo scroll, una clase agregada lo hace mas pequeño
            navbarCollapsible.classList.add('navbar-shrink')
        }

    };

    // Ejecuta para definir el tamaño del navbar al innicio 
    navbarShrink();

    // Ejecuta cuando se hace scroll
    document.addEventListener('scroll', navbarShrink);

    // Resalta el elemento o seccion que el usaurio está viendo en el momento
    const mainNav = document.body.querySelector('#mainNav');
    if (mainNav) {
        new bootstrap.ScrollSpy(document.body, {
            target: '#mainNav',
            rootMargin: '0px 0px -40%',
        });
    };

    // Cierra el menú cuando el usuario hace click en un enlace
    const navbarToggler = document.body.querySelector('.navbar-toggler');
    const responsiveNavItems = [].slice.call(
        document.querySelectorAll('#navbarResponsive .nav-link')
    );
    responsiveNavItems.map(function (responsiveNavItem) {
        responsiveNavItem.addEventListener('click', () => {
            if (window.getComputedStyle(navbarToggler).display !== 'none') {
                navbarToggler.click();
            }
        });
    });

    //elementos necesarios para el calendario
    const monthDisplay = document.getElementById('monthDisplay');  //muestra mes y año  
    const daysContainer = document.getElementById('calendarDays'); //cuenta los días
    const prevBtn = document.getElementById('prevMonth'); //boton para mes anterior
    const nextBtn = document.getElementById('nextMonth'); //boton para el mes siguiente
    // Inicializa la fecha con el mes y año actual
    let date = new Date();
    //genera el calendario del mes 
    function renderCalendar() {
        if (!daysContainer) return; // Si no hay calendario en la página, no hace nada
        // Limpia el contenido anterior antes de redibujar
        daysContainer.innerHTML = "";
        const month = date.getMonth();
        const year = date.getFullYear();
        const monthNames = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"];
        // Muestra el nombre del mes y el año actual
        monthDisplay.innerText = `${monthNames[month]} ${year}`;

        const firstDayIndex = new Date(year, month, 1).getDay(); // Calcula en qué día de la semana empieza el mes 
        const lastDay = new Date(year, month + 1, 0).getDate(); // Calcula cuántos días tiene el mes

        for (let x = firstDayIndex; x > 0; x--) {
            daysContainer.appendChild(document.createElement("div"));
        }

        // Agrega un div por cada día del mes
        for (let i = 1; i <= lastDay; i++) {
            const dayDiv = document.createElement("div");
            dayDiv.innerText = i;
            const today = new Date();
             // Marca el día actual con la clase 'today' para resaltarlo visualmente
            if (i === today.getDate() && month === today.getMonth() && year === today.getFullYear()) {
                dayDiv.classList.add("today");
            }
            daysContainer.appendChild(dayDiv);
        }
    }

    if (prevBtn && nextBtn) {
        // Al hacer click en anterior, resta un mes y vuelve a renderizar el calendario
        prevBtn.addEventListener('click', () => {
            date.setMonth(date.getMonth() - 1);
            renderCalendar();
        });
        // Al hacer click en siguiente, suma un mes y vuelve a renderizar el calendario
        nextBtn.addEventListener('click', () => {
            date.setMonth(date.getMonth() + 1);
            renderCalendar();
        });
    }
// Renderiza el calendario al cargar la página
    renderCalendar();

    // Antes de enviar el formulario de compra verifica que
    // al menos un checkbox de obra esté seleccionado
    const formCompra = document.getElementById('formCompra');
    if (formCompra) {
        formCompra.addEventListener('submit', function(e) {
            const checkboxes = document.querySelectorAll('input[name="artworkIds"]:checked');
            // Si no hay ninguno seleccionado, cancela el envío y muestra el error
            if (checkboxes.length === 0) {
                e.preventDefault();
                document.getElementById('errorObras').style.display = 'block';
            }
        });
    }

    if (window.location.search.includes('filter') || window.location.search.includes('mail')) {
        
        // Si la URL contiene "/venta/buscar", al limpiar queremos que quede en "/venta" o "/"
        // Evaluamos en qué sección del sitio está el usuario:
        let rutaLimpia = '/';
        
        if (window.location.pathname.includes('/venta')) {
            rutaLimpia = '/venta'; // Lo deja en la sección de ventas limpia
        }

        // Cambia la barra de direcciones estéticamente sin recargar la vista actual
        window.history.replaceState({}, document.title, rutaLimpia);
    }

    //hace scroll a la seccion de venta cuando ocurre una acción
    
    const urlParams = new URLSearchParams(window.location.search);
    const message = urlParams.get('message');
    
    if (message && message.startsWith('venta-')) {
        const ventaSection = document.getElementById('venta');
        if (ventaSection) {
            ventaSection.scrollIntoView({ behavior: 'smooth' });
        }
    }

    // Mostrar panel admin automáticamente si tiene un mensaje
    const adminPanel = document.getElementById('panel-admin');
    if (adminPanel && adminPanel.querySelector('.alert')) {
        adminPanel.style.display = 'block';
        adminPanel.scrollIntoView({ behavior: 'smooth' });
    }

        // Limpiar ?message= de la URL sin recargar
    if (window.location.search.includes('message')) {
        const url = new URL(window.location.href);
        url.searchParams.delete('message');
        window.history.replaceState({}, '', url.toString());
    }
    
});

//calcula los valores del total en tiempo real segun los checks realizados

function calcularTotal() {
    const checkboxes = document.querySelectorAll('input[name="artworkIds"]:checked');
    let subtotal = 0;

    checkboxes.forEach(function(checkbox) {
        const label = document.querySelector('label[for="' + checkbox.id + '"]');
        const precioSpan = label.querySelector('[data-price]');
        subtotal += parseFloat(precioSpan.getAttribute('data-price'));
    });

    let discount = 0;
    if (checkboxes.length >= 3) {
        discount = subtotal * 0.10;
    }
    if (subtotal > 3000000000) {
        discount = subtotal * 0.15;
    }

    const total = subtotal - discount;

    document.getElementById('subtotal').textContent = subtotal.toLocaleString('es-CO');
    document.getElementById('total').textContent = total.toLocaleString('es-CO');

    if (discount > 0) {
        document.getElementById('descuentoTexto').style.display = 'block';
        document.getElementById('descuento').textContent = discount.toLocaleString('es-CO');
    } else {
        document.getElementById('descuentoTexto').style.display = 'none';
    }
}

//despliega y oculta el menú de admin
function toggleAdmin() {
    const panel = document.getElementById('panel-admin');

    if (panel.style.display === 'block') {
        panel.style.display = 'none';
    } else {
        panel.style.display = 'block';
        panel.scrollIntoView({ behavior: 'smooth' });
    }
}

//controla el botón de las obras
//cambia dependiendo de la acción
function toggleObras(btn) {
    const mas = document.getElementById('masObras');
    if (mas.style.display === 'none') {
        mas.style.display = 'block';
        btn.textContent = 'Ver menos obras';
    } else {
        mas.style.display = 'none';
        btn.textContent = 'Ver todas las obras';
    }
}