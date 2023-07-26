$('document').ready(function (){
    $('table #editBtn').on('click', function (event){
        event.preventDefault();
        var href = $(this).attr('href');
        $.get(href, function (categoryNew, status){
            $('#idEdit').val(categoryNew.id);
            $('#nameEdit').val(categoryNew.name);
        });
        $('#editModal').modal();
    });
});