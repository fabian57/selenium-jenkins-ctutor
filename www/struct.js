// Variables

let structName = "";
let structDesc = "";
let members = {};
let membersDesc = {};
let returnType = "";
let returnTypeDesc;
let isStatic = false;
let datatypes = {};

// Struct controllers

function changeStructName() {
    structName = document.getElementById('structName').value;
    if (structName == "") {
        document.getElementById('structNameSpan').innerHTML = "struct_name";
        document.getElementById('structNameOut').innerHTML = "Strukturname noch nicht festgelegt.";
    } else {
        document.getElementById('structNameSpan').innerHTML = structName;
        document.getElementById('structNameOut').innerHTML = structName;
    }
}

function changeStructDesc() {
    structDesc = document.getElementById('structDescIn').value;
    if (structDesc == "") {
        document.getElementById('structDescOut').innerHTML = "Struktur nicht beschrieben.";
    } else {
        document.getElementById('structDescOut').innerHTML = structDesc;
    }
}

function toggleVisibility() {
    if (document.getElementById('static').checked) {
        document.getElementById('visibilityOut').innerHTML = "static ";
    } else {
        document.getElementById('visibilityOut').innerHTML = "";
    }
}

// Use membereters

function showMemberDef() {
    document.getElementById('addMember').style.display = 'block';
}

function addMember() {
    let name = "";
    let type = "";
    let isList = document.getElementById('list').checked;
    name = document.getElementById('memberName').value;
    type = document.getElementById('memberType').value;
    console.log(type);
    if (name in members) {
        members[name] = {type, isList};
    } else {
        members[name] = {type, isList};
        let select = document.getElementById('MemberList');
        let option = document.createElement('option');
        for (member in members) {
            option.value = member;
            option.innerHTML = member;
            select.appendChild(option);
        }
    }
    document.getElementById('rm').style.display = 'block';
    printMembers();
    addMemberDesc();
    
}

function addMemberDesc() {
    let name = "";
    let desc = "";
    name = document.getElementById('memberName').value;
    desc = document.getElementById('membersDescIn').value;
    membersDesc[name] = desc;
    printMembersDesc();
}

function rmMember() {
    let select = document.getElementById('MemberList');
    let name = select.value;
    delete members[name];
    delete membersDesc[name];
    select.remove(select.selectedIndex);

    if (Object.keys(members).length == 0) {
        document.getElementById('rm').style.display = 'none';
    }
    printMembers();
    printMembersDesc();
}

function printMembers() {
    let numMembers = Object.keys(members).length ;
    let currentNumCommas = 0;
    let type = "";
    
    if (type == "string") {
        type = "const char*";
    }

    if (numMembers == 0) {
        document.getElementById('membersSpan').innerHTML = "";
    } else {
        document.getElementById('membersSpan').innerHTML = "";
        for (member in members) {
            if (typeof members[member] !== 'undefined') {
                type = members[member].type;
            }
            document.getElementById('membersSpan').innerHTML += "&emsp;";
            if (!members[member].isList) {
                document.getElementById('membersSpan').innerHTML += type+" "+member;
            } else {
                document.getElementById('membersSpan').innerHTML += "const "+type+"* "+member+", <br>&emsp;int "+member+"_size";
            }
            if (currentNumCommas < numMembers-1) {
                document.getElementById('membersSpan').innerHTML += ", <br>";
                currentNumCommas++;
            } else {
                document.getElementById('membersSpan').innerHTML += "<br>";
            }
        }
    }
}

function printMembersDesc() {
    document.getElementById('memberDescOut').innerHTML = "";
    let numMembers = Object.keys(members).length;
    if (numMembers == 0) {
        document.getElementById('memberDescOut').innerHTML += "&nbsp;* <br>";
    } else {
        for (desc in membersDesc) {
            document.getElementById('memberDescOut').innerHTML += "&nbsp;* \\member "+desc+" "+membersDesc[desc]+"<br>";
        }
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
    let standardSelect = document.getElementById('memberType');
    let option = document.createElement('option');
    option.value = document.getElementById('typeName').value;
    option.innerHTML = document.getElementById('typeName').value;
    customSelect.appendChild(option.cloneNode(true));
    standardSelect.appendChild(option.cloneNode(true));
}

function rmType() {
    let customSelect = document.getElementById('typeList');
    let standardSelect = document.getElementById('memberType');
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
