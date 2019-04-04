// Variables
let modulName = "";
let modulDesc = "";

function changeModulName() {
    modulName = document.getElementById('modulName').value;
    let elems = document.getElementsByClassName('modulNameOut');
    if (modulName == "") {
        for (i in elems) {
            elems[i].innerHTML = "Modulname";
        }
    } else {
        for (i in elems) {
            elems[i].innerHTML = modulName;
        }
    }
}

function changeModulDesc() {
    modulDesc = document.getElementById('modulDescIn').value;
    if (modulDesc == "") {
        document.getElementById('modulDescOut').innerHTML = "";
    } else {
        document.getElementById('modulDescOut').innerHTML = modulDesc;
    }
}

function unitTest() {
    let isChecked = document.getElementById('unit').checked;
    if (isChecked) {
        document.getElementById('unitOut').style.display = "block";
    } else {
        document.getElementById('unitOut').style.display = "none";
    }
}

function iostream() {
    let isChecked = document.getElementById('io').checked;
    if (isChecked) {
        document.getElementById('ioOut').style.display = "block";
    } else {
        document.getElementById('ioOut').style.display = "none";
    }
}

// Put content of #src into temporary textarea and copy to clipboard
function copy(divId) {
    let textarea = document.createElement('textarea');
    textarea.id = 'temp_element';
    textarea.style.height = 0;
    document.body.appendChild(textarea);
    textarea.value = document.getElementById(divId).innerText;
    let selector = document.querySelector('#temp_element');
    selector.select();
    document.execCommand('copy');
    document.body.removeChild(textarea);
}