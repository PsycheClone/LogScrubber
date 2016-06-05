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

    var rows = $(tables[0]).children().find("tr");
    firstArray = rows.splice(1,rows.length);

    //Iterate the tables
    for (var i = 1; i < tables.length; i++) {
        rows = $(tables[i]).children().find("tr");
        secondArray = rows.splice(1,rows.length);

        //Iterate the firstTable
        for (var j = 0; j < firstArray.length; j++) {
            firstTag = $($(firstArray[j]).find("td")[0]).html();
            firstAverage = parseInt($($(firstArray[j]).find("td")[1]).html());

            //Iterate the secondTable
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
        firstArray = secondArray;
        secondArray = [];
    }
}

function generateDatasetTables(host, from) {
    $("#tableContainer").children().detach();
    $.get("http://localhost:8090/tablechart?host=" + host +"&from=" + from + "&range=60", function(data) {
        var template = Handlebars.compile($("#tableDatasetTemplate").html());
        var html = template(data);
        $("#tableContainer").append(html);

        colorTables();
    });
}