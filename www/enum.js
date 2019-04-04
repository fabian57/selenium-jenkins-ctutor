// Variables
let enumName = "";
let enumDesc = "";
let consts = {};
let constsDesc = {};

function changeEnumName() {
    enumName = document.getElementById('enumName').value;
    if (enumName == "") {
        document.getElementById('enumerationNameSpan').innerHTML = "enum_name";
        document.getElementById('enumNameOut').innerHTML = "Enumerationssname noch nicht festgelegt.";
    } else {
        document.getElementById('enumerationNameSpan').innerHTML = enumName;
        document.getElementById('enumNameOut').innerHTML = enumName;
    }
}

function changeEnumDesc() {
    enumDesc = document.getElementById('enumDescIn').value;
    if (enumDesc == "") {
        document.getElementById('enumDescOut').innerHTML = "Enumeration nicht beschrieben.";
    } else {
        document.getElementById('enumDescOut').innerHTML = enumDesc;
    }
}

function addConst() {
    let name = "";
    let constVal = "";
    name = document.getElementById('constName').value;
    constVal = document.getElementById('constValue').value;
    if (name in consts) {
        consts[name] = constVal;
    } else {
        consts[name] = constVal;
        let select = document.getElementById('ConstList');
        let option = document.createElement('option');
        for (constant in consts) {
            option.value = constant;
            option.innerHTML = constant;
            select.appendChild(option);
        }
    }
    document.getElementById('rm').style.display = 'block';
    console.log(consts);
    printConsts();
}

function rmConst() {
    let select = document.getElementById('ConstList');
    let name = select.value;
    delete consts[name];
    select.remove(select.selectedIndex);

    if (Object.keys(consts).length == 0) {
        document.getElementById('rm').style.display = 'none';
    }
    printConsts();
}

function printConsts() {
    let numConsts = Object.keys(consts).length ;
    let currentNumCommas = 0;

    if (numConsts == 0) {
        document.getElementById('constantsSpan').innerHTML = "";
    } else {
        document.getElementById('constantsSpan').innerHTML = "";
        for (constant in consts) {
            let constValString = "";
            if (consts[constant] !== '') {
                constValString = " = "+consts[constant];
            }
            if (!consts[constant].isList) {
                document.getElementById('constantsSpan').innerHTML += "&emsp;"+constant+constValString;
            }
            if (currentNumCommas < numConsts-1) {
                document.getElementById('constantsSpan').innerHTML += ", <br>";
                currentNumCommas++;
            } else {
                document.getElementById('constantsSpan').innerHTML += "<br>";
            }
        }
    }
}


// Put content of #src into temporary textarea and copy to clipboard
function copy() {
    let textarea = document.createElement('textarea');
    textarea.id = 'temp_element';
    textarea.style.height = 0;
    document.body.appendChild(textarea);
    textarea.value = document.getElementById('src').innerText;
    let selector = document.querySelector('#temp_element');
    selector.select();
    document.execCommand('copy');
    document.body.removeChild(textarea);
}