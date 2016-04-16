var dashboard = new google.visualization.Dashboard(document.getElementById('dashboard'));
var dataTable = new google.visualization.DataTable();
var options;
var tags;

google.visualization.events.addListener(dashboard, 'ready', function () {
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

function drawBasic() {
    dataTable.addColumn('string', 'Tag');
    dataTable.addColumn('number', 'Average');
    // A column for custom tooltip content
    dataTable.addColumn({type: 'string', role: 'tooltip'});

    for (var i in tags.logLines) {
        var row = tags.logLines[i];
        dataTable.addRow([row.tag, row.average, row.tag + "\n" + row.average + " seconds."]);
    }

    options = { 'title': tags.range,
        'tooltip': { isHtml: true },
        'chartArea': {'width': '60%', 'height': '80%', 'left': '40%'},
        'titleTextStyle': {
            fontSize: '30'
        },
        'legend': 'none',
        'hAxis': {
            direction: 1,
            title: 'Seconds',
            minValue: 0
        },
        'vAxis': {
        }};

    var stringFilter = new google.visualization.ControlWrapper({
        'controlType': 'StringFilter',
        'containerId': 'stringControl',
        'options': {
            'matchType': 'any',
            'caseSensitive': 'false',
            'filterColumnLabel': 'Tag'
        }
    });

    var table = new google.visualization.ChartWrapper({
        'chartType': 'BarChart',
        'containerId': 'tableChart',
        'options': options
    });

    dashboard.bind(stringFilter, table);
}