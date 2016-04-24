//    <![CDATA[
var dashboard = new google.visualization.Dashboard(document.getElementById('dashboard'));
var barchart = new google.visualization.ChartWrapper({
    'chartType': 'BarChart',
    'containerId': 'tableChart'
});
var stringFilter = new google.visualization.ControlWrapper({
    'controlType': 'StringFilter',
    'containerId': 'stringControl',
    'options': {
        'matchType': 'any',
        'caseSensitive': 'false',
        'filterColumnLabel': 'Tag'
    }
});
var dataTable = new google.visualization.DataTable();

google.visualization.events.addListener(barchart, 'select', selectHandler);
google.visualization.events.addListener(dashboard, 'ready', function () {

    //moving search input every time the chart draws itself
    $(".google-visualization-controls-label").remove();
    var inputField = $("#4-input").detach();
    $("#search").append(inputField);
    inputField.attr("placeholder", "Filter...");
    inputField.attr("class", "form-control inputMoved");
    inputField.attr("id", "searchInput");

    if($(".inputMoved").length > 1) {
        $(".inputMoved")[0].remove();
    }
    inputField.focus();
});

function fillDatatable(barchartData) {
    dataTable.addColumn('string', 'Tag');
    dataTable.addColumn('number', 'Average');
    // A column for custom tooltip content
    dataTable.addColumn({type: 'string', role: 'tooltip'});

    for (var i in barchartData.dataset) {
        var row = barchartData.dataset[i];
        dataTable.addRow([row.tag, row.average, row.tag + "\n" + row.average + " seconds over " + row.count + " times."]);
    }

    $("#rangeTitle").text(barchartData.range);
}

function basicSetup() {
    var height = dataTable.getNumberOfRows() * 41 + 30;

    var options = {
        'title': null,
        'tooltip': {isHtml: true},
        'chartArea': {'width': '60%', 'top': 0, 'bottom': 30, 'left': '40%'},
        'legend': {'position': 'in', 'alignment': 'end'},
        'height': height,
        'hAxis': {
            direction: 1,
            title: 'Seconds',
            minValue: 0
        },
        'vAxis': {}
    };

    barchart.setOptions(options);
    dashboard.bind(stringFilter, barchart);
}

function selectHandler() {
    var selectedItem = barchart.getChart().getSelection()[0];
    if (selectedItem) {
        var topping = dataTable.getValue(selectedItem.row, 0);
        alert('The user selected ' + topping);
    }
}

function drawTable() {
    dashboard.draw(dataTable);
}
//    ]]>