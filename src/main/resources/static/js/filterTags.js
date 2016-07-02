$(function() {
    $("#filterQuery").click(function() {
        $(this).select();
    });
    
    $("#filterSubmit").click(function() {
        var query = $("#filterQuery").val();
        filterTags(query);
    });
    
    $("#clearFilter").click(function() {
        $("#filterQuery").val("");
        filterTags("");
    });
    
    $("#filterQuery").keypress(function(event) {
        if (event.which == 13) {
            event.preventDefault();
            var query = $("#filterQuery").val();
            filterTags(query);
        }
    });
})

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