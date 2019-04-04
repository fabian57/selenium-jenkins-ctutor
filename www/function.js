// Variables

let funcName = "";
let funcDesc = "";
let params = {};
let paramsDesc = {};
let returnType = "";
let returnTypeDesc;
let isStatic = false;
let datatypes = {};

// Function controllers

function changeFuncName() {
    funcName = document.getElementById('funcName').value;
    if (funcName == "") {
        document.getElementById('functionNameSpan').innerHTML = "function_name";
        document.getElementById('funcNameOut').innerHTML = "Funktionsname noch nicht festgelegt.";
    } else {
        document.getElementById('functionNameSpan').innerHTML = funcName;
        document.getElementById('funcNameOut').innerHTML = funcName;
    }
}

function changeFuncDesc() {
    funcDesc = document.getElementById('funcDescIn').value;
    if (funcDesc == "") {
        document.getElementById('funcDescOut').innerHTML = "Funktion nicht beschrieben.";
    } else {
        document.getElementById('funcDescOut').innerHTML = funcDesc;
    }
}

function toggleVisibility() {
    if (document.getElementById('static').checked) {
        document.getElementById('visibilityOut').innerHTML = "static ";
    } else {
        document.getElementById('visibilityOut').innerHTML = "";
    }
}

// Use parameters

function showParamDef() {
    document.getElementById('addParam').style.display = 'block';
}

// Don't use parameters

function hideParamDef() {
    document.getElementById('addParam').style.display = 'none';
    params = {};
    paramsDesc= {};
    document.getElementById('ParamList').options.length = 0;
    printParams();
    printParamsDesc();
}

function addParam() {
    let name = "";
    let type = "";
    let isList = document.getElementById('list').checked;
    name = document.getElementById('paramName').value;
    type = document.getElementById('paramType').value;
    if (name in params) {
        params[name] = {type, isList};
    } else {
        params[name] = {type, isList};
        let select = document.getElementById('ParamList');
        let option = document.createElement('option');
        for (param in params) {
            option.value = param;
            option.innerHTML = param;
            select.appendChild(option);
        }
    }
    document.getElementById('rm').style.display = 'block';
    printParams();
    addParamDesc();
    
}

function addParamDesc() {
    let name = "";
    let desc = "";
    name = document.getElementById('paramName').value;
    desc = document.getElementById('paramsDescIn').value;
    paramsDesc[name] = desc;
    printParamsDesc();
}

function rmParam() {
    let select = document.getElementById('ParamList');
    let name = select.value;
    delete params[name];
    delete paramsDesc[name];
    select.remove(select.selectedIndex);

    if (Object.keys(params).length == 0) {
        document.getElementById('rm').style.display = 'none';
    }
    printParams();
    printParamsDesc();
}

function printParams() {
    let numParams = Object.keys(params).length ;
    let currentNumCommas = 0;
    let type = "";
    
    if (type == "string") {
        type = "const char*";
    }

    if (numParams == 0) {
        document.getElementById('parametersSpan').innerHTML = "";
    } else {
        document.getElementById('parametersSpan').innerHTML = "";
        for (param in params) {
            if (typeof params[param] !== 'undefined') {
                type = params[param].type;
            }
            if (!params[param].isList) {
                document.getElementById('parametersSpan').innerHTML += type+" "+param;
            } else {
                document.getElementById('parametersSpan').innerHTML += "const "+type+"* "+param+", int "+param+"_size";
            }
            if (currentNumCommas < numParams-1) {
                document.getElementById('parametersSpan').innerHTML += ", ";
                currentNumCommas++;
            }
        }
    }
}

function printParamsDesc() {
    document.getElementById('paramsDescOut').innerHTML = "";
    let numParams = Object.keys(params).length;
    if (numParams == 0) {
        document.getElementById('paramsDescOut').innerHTML += "&nbsp;* <br>";
    } else {
        for (desc in paramsDesc) {
            document.getElementById('paramsDescOut').innerHTML += "&nbsp;* \\param "+desc+" "+paramsDesc[desc]+"<br>";
        }
    }
}

function returnTypeFunc() {
    returnType = document.getElementById('returnType').value;
    if (returnType == "none") {
        document.getElementById('returnTypeSpan').innerHTML = "return_type";
    } else {
        document.getElementById('returnTypeSpan').innerHTML = returnType;
    }
}

function changeReturnDesc() {
    funcDesc = document.getElementById('returnDescIn').value;
    if (funcDesc == "") {
        document.getElementById('returnDescOut').innerHTML = "Funktion nicht beschrieben.";
    } else {
        document.getElementById('returnDescOut').innerHTML = funcDesc;
    }
}

function addType() {
    let type = "";
    if (document.getElementById('struct').checked) {
        type = "struct";
    } else {
        type = "enum";
    }
    datatypes[document.getElementById('typeName').value] = type;
    
    let customSelect = document.getElementById('typeList');
    let standardSelect = document.getElementById('paramType');
    let option = document.createElement('option');
    option.value = document.getElementById('typeName').value;
    option.innerHTML = document.getElementById('typeName').value;
    customSelect.appendChild(option.cloneNode(true));
    standardSelect.appendChild(option.cloneNode(true));
}

function rmType() {
    let customSelect = document.getElementById('typeList');
    let standardSelect = document.getElementById('paramType');
    let name = customSelect.value;
    delete datatypes[name];
    customSelect.remove(customSelect.selectedIndex);

    for (i = 0; i < standardSelect.length; i++) {
        if (standardSelect.options[i].value == name) {
            standardSelect.remove(standardSelect.options[i].index);
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
