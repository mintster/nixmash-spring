/*<![CDATA[*/

function retrieveGuests() {
    var url = '/json/cod/1';

    var result = load(url);

    showDialog(result);
}

function showDialog(result) {

    BootstrapDialog.show({
        type: BootstrapDialog.TYPE_PRIMARY,
        title: "Contact of the Day",
        message: result,
        draggable: true,
        buttons: [{
            label: 'Close',
            cssClass: 'btn-info',
            action: function (dialogItself) {
                dialogItself.close();
            }
        }]
    })

}

/*]]>*/
