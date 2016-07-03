var dataset = {};

function colorTables() {
    var firstArray = [];
    var secondArray = [];
    var firstTag;
    var firstAverage;
    var firstCount;
    var secondTag;
    var secondAverage;
    var secondCount;
    var tables = $(".datasetTable");

    //Get the first table for referencing the second
    var rows = $(tables[0]).children().find("tr");
    //Get the rows without the header
    firstArray = rows.splice(1,rows.length);

    //Iterate the tables
    for (var i = 1; i < tables.length; i++) {
        rows = $(tables[i]).children().find("tr");
        secondArray = rows.splice(1,rows.length);

        //Iterate the table to compare to : firstArray of rows
        for (var j = 0; j < firstArray.length; j++) {
            firstTag = $($(firstArray[j]).find("td")[0]).html();
            firstAverage = parseInt($($(firstArray[j]).find("td")[1]).html());

            //Iterate the table to compare with the firstArray
            for (var k = 0; k < secondArray.length; k++) {
                secondTag = $($(secondArray[k]).find("td")[0]).html();
                secondAverage = parseInt($($(secondArray[k]).find("td")[1]).html());

                if(firstTag === secondTag) {
                    if(secondAverage > firstAverage) {
                        $(secondArray[k]).addClass("danger");
                    } else {
                        $(secondArray[k]).addClass("success");
                    }
                    break;
                }
            }
        }
        //Move on to the next table comparison
        firstArray = secondArray;
        secondArray = [];
    }
}

function renderTables(data) {
    $("#tableContainer").children().detach();
    var template = Handlebars.compile($("#tableDatasetTemplate").html());
    var html = template(data);
    $("#tableContainer").append(html);
    $(".datasetTable").dataTable({
        paging: false,
        searching: false,
        info: false
    });
    colorTables();
}

function generateDatasetTables(host, from, till, slice) {
    var jqxhr = $.get("http://localhost:8090/tablechart?host=" + host +"&from=" + from + "&till=" + till + "&slice=" + slice, function(data) {
    })
        .done(function(data) {
            dataset = data;
            if (typeof dataset.rangeDatasets !== 'undefined' && dataset.rangeDatasets.length > 0) {
                $("#filterForm").show();
            } else {
                $("#filterForm").hide();
            }
            renderTables(dataset);
            $("#loaderspinner").hide();
            $("#tableContainer").show();
        })
        .fail(function(data) {
            $("#filterForm").hide();
            $("#loaderspinner").hide();
            $("#placeholderContainer").show();
            var n = $("#notifyContainer").noty({
                layout: 'topCenter',
                timeout: 10000,
                type: 'error',
                text: data.responseText,
                animation: {
                    open: 'animated fadeInDown', // Animate.css class names
                    close: 'animated fadeOutDown', // Animate.css class names
                    easing: 'swing', // unavailable - no need
                    speed: 500 // opening & closing animation speed
                }});
        })
}

$(function() {
    $("#selectorFormSubmit").click(function() {
        $("#tableContainer").hide();
        $("#placeholderContainer").hide();
        $("#loaderspinner").show();

        var formComponents = $("#selectorForm").serializeArray();
        var fromTill = formComponents[1].value.split(" - ");
        var from = fromTill[0];
        var till = fromTill[1];
        generateDatasetTables(formComponents[0].value, from, till, formComponents[2].value);
    });
});