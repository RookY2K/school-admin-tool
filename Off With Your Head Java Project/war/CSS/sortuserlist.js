/** This File Is Used To SORT the User List **/

function viewProfile(row) {
	var table = document.getElementById('users');
    document.getElementById('firstnamemodal').innerHTML = table.rows[row].cells[1].innerHTML;
    document.getElementById('lastnamemodal').innerHTML = table.rows[row].cells[0].innerHTML;
    document.getElementById('emailmodal').innerHTML = table.rows[row].cells[2].innerHTML;
    document.getElementById('phonemodal').innerHTML = table.rows[row].cells[3].innerHTML;
    document.getElementById('addressmodal').innerHTML = table.rows[row].cells[4].innerHTML;
    document.getElementById('edituserprofilefromview').value = table.rows[row].cells[2].innerHTML;
}

function editProfile(row) {
	var table = document.getElementById('users');
    document.getElementById('firstname').value = table.rows[row].cells[1].innerHTML;
    document.getElementById('lastname').value = table.rows[row].cells[0].innerHTML;
    document.getElementById('email').value = table.rows[row].cells[2].innerHTML;
    document.getElementById('username').value = table.rows[row].cells[2].innerHTML;
    document.getElementById('phone').value = table.rows[row].cells[3].innerHTML;
    document.getElementById('streetaddress').value = table.rows[row].cells[5].innerHTML;
    document.getElementById('city').value = table.rows[row].cells[6].innerHTML;
    document.getElementById('state').value = table.rows[row].cells[7].innerHTML;
    document.getElementById('zip').value = table.rows[row].cells[8].innerHTML;
}