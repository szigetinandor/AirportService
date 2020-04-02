class BSAlert {
    constructor(element) {
        this.element = element;
    }

    printAlert(response) {
        this.setAlertType(response.alertType);
        this.element.html('');
        this.element.fadeIn(500);
        var self = this;
        $.each(response.message, function(key, value) {
            self.element.append('<p>' + value + '</p>');
        });
        setTimeout(function() {
            self.hideAlert();
        }, 5000);
    }

    setAlertType(type) {
        this.element.removeClass (function (index, className) {
            return (className.match (/(^|\s)alert-\S+/g) || []).join(' ');
        });
        this.element.addClass('alert-' + type);
    }

    hideAlert() {
        this.element.fadeOut(500);
    }
}

const dtLanguage = {
    emptyTable: 'Nincs rendelkezésre álló adat.',
    info: 'Megjelenítve: _START_ - _END_, összesen _TOTAL_',
    infoEmpty: '',
    infoFiltered: '(_MAX_ termékből szűrve)',
    lengthMenu: '_MENU_ megjelenítése',
    loadingRecords: 'Betöltés...',
    processing: 'Feldolgozás...',
    search: 'Keresés:',
    zeroRecords: 'Nincs találat.',
    paginate: {
        first: 'Első',
        last: 'Utolsó',
        next: '<i class="fas fa-chevron-right"></i>',
        previous: '<i class="fas fa-chevron-left"></i>'
    }
};

let departuresTable;
let arrivalsTable;

$(document).ready( function () {
    departuresTable = $('#departures').DataTable({
        ajax: '/api/airports/1/departures',
        columns: [
            {
                title: 'Flight',
                data: 'flight'
            },
            {
                title: 'Terminal',
                data: 'from_terminal'
            },
            {
                title: 'Gate',
                data: 'from_gate'
            },
            {
                title: 'To',
                data: 'to'
            },
            {
                title: 'time',
                data: 'departure'
            },
            {
                title: 'Remark',
                data: "remark"
            },
            {
                title: 'Interactions',
                render: function(data, type, row) {
                    return drawButtons('showUpdateFlightForm', 'flights', row);
                },
                orderable: false,
                searchable: false,
                className: 'actions-column'
            }
        ],
        language: dtLanguage,
    });

    arrivalsTable = $('#arrivals').DataTable({
        ajax: '/api/airports/1/arrivals',
        columns: [
            {
                title: 'Flight',
                data: 'flight'
            },
            {
                title: 'Terminal',
                data: 'to_terminal'
            },
            {
                title: 'Gate',
                data: 'to_gate'
            },
            {
                title: 'To',
                data: 'from'
            },
            {
                title: 'time',
                data: 'arrival'
            },
            {
                title: 'Remark',
                data: "remark"
            },
            {
                title: 'Interactions',
                render: function(data, type, row) {
                    return drawButtons('showUpdateFlightForm', 'flights', row);
                },
                orderable: false,
                searchable: false,
                className: 'actions-column'
            }
        ],
        language: dtLanguage,
    });

    setInterval(function(){
        departuresTable.ajax.reload();
        arrivalsTable.ajax.reload();
    }, 10000);
} );



function showProducts() {
    hideAll();
    setActive('products');
    $('#add-button').attr('onclick', 'event.preventDefault();showAddProductForm()');
    departuresTable.ajax.reload();
    $('#products-container').show();
}

function drawButtons(updateFunc, type, row) {

    return `
        <div class="d-inline-block mx-2">
            <a href="" onclick="event.preventDefault();${updateFunc}(${row.id});">
                <i class="fas fa-pen"></i>
            </a>
        </div>
        <div class="d-inline-block mx-2">
            <a href="" onclick="event.preventDefault();showDeleteModal('${type}', '${row.id}', '${row.name}');">
                <i class="far fa-trash-alt"></i>
            </a>
        </div>`;
}

function showDeleteModal(action, id, name) {
    $('#deleteForm').attr('action', `/${action}/delete/${id}`);
    $('#deleteModalText').html('Biztosan törölni szeretnéd a(z) "' + name + '" elemet?');
    $('#deleteModal').modal('show');
}

function hideAll() {
    $('.page-control').each(function() {
        $(this).hide();
    });
    $('.needs-reset').trigger('reset');
}

let bsAlert = new BSAlert($('#alert'));

$(".ajax-form").submit(function(e) {
    e.preventDefault();
    var form = $(this);
    var back = $(this).find('.back-button');
    var url = form.attr('action');
    $.ajax({
        url: url,
        type: "POST",
        dataType: 'json',
        data: form.serialize(),
        success: function(data) {
            back.click();
            departuresTable.ajax.reload();
            arrivalsTable.ajax.reload();
            bsAlert.printAlert(data);
        },
        error: function(data) {
            bsAlert.printAlert(data.responseJSON);
        }
    });
});

function addItems(element, url, selected) {
    element.html('');
    $.ajax({
        url: url,
        type: 'GET',
        dataType: 'json',
        success: function(items) {
            items.data.forEach(function(item) {
                element.append(`<option value="${item.id}">${item.name}</option>`);
            });
            if(selected)
                element.val(selected).change();
        }
    });
}

function showAddFlightForm() {
    hideAll();
    $('#flight-form').attr('action', '/api/flights/add');
    addAirportsToForm();
    $('#flight-form-container').show();
}

function addAirportsToForm() {
    addItems($('#from'), '/api/airports');
    addItems($('#to'), '/api/airports');
}