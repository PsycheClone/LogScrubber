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

    colorTables();
}

function filterTags(query) {
    var filtered = {};
    var rangeDatasets = $.extend(true, [], dataset.rangeDatasets);
    for(var i = 0; i < rangeDatasets.length; i++) {
        var datasets = rangeDatasets[i].dataset;
        var j = datasets.length;
        while(j--) {
            var tag = datasets[j].tag;
            if(tag.toLowerCase().indexOf(query.toLowerCase()) < 0) {
                datasets.splice(j, 1);
            }
        }
    }
    filtered.rangeDatasets = rangeDatasets;
    renderTables(filtered);
}

function generateDatasetTables(host, from, till, slice) {
    $.get("http://localhost:8090/tablechart?host=" + host +"&from=" + from + "&till=" + till + "&slice=" + slice, function(data) {
        dataset = data;
        if(typeof dataset.rangeDatasets !== 'undefined' && dataset.rangeDatasets.length > 0) {
            $("#filterForm").show();
        } else {
            $("#filterForm").hide();
        }
        renderTables(dataset);
        $("#loaderspinner").hide();
        $("#tableContainer").show();
        $(".datasetTable").dataTable({
            paging: false,
            searching: false,
            info: false
        });
    });
}