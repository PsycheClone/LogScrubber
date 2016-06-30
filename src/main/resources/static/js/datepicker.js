$(function() {
    var currentTime = moment();
    var formattedTime = currentTime.format('YYYY-MM-DD HH:mm');

    $('#daterange').daterangepicker({
        "showWeekNumbers": true,
        "timePicker": true,
        "timePicker24Hour": true,
        "startDate": currentTime,
        "endDate": currentTime,
        "ranges": {
            'Today': [moment(), moment()],
            'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
            'Last 7 Days': [moment().subtract(6, 'days'), moment()],
            'Last 30 Days': [moment().subtract(29, 'days'), moment()],
            'This Month': [moment().startOf('month'), moment().endOf('month')],
            'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
        },
        "alwaysShowCalendars": true,
        "opens": "left"
    }, function (start, end, label) {
    });

    $('input[name="datefilter"]').val(formattedTime + ' - ' + formattedTime);

    $('input[name="datefilter"]').on('apply.daterangepicker', function (ev, picker) {
        $(this).val(picker.startDate.format('YYYY-MM-DD HH:mm') + ' - ' + picker.endDate.format('YYYY-MM-DD HH:mm'));
    });

    $('input[name="datefilter"]').on('cancel.daterangepicker', function (ev, picker) {
        $(this).val('');
    });
})