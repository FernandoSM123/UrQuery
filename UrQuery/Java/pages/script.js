//CONSTANTES
const editionArea = document.getElementById("EA");
const documentArea = document.getElementById("DA");
const resultArea = document.getElementById("RA");

const popUpAbout = document.getElementById('popUpAbout');
const aboutBody = document.getElementById('aboutBody');

const okLabel = document.getElementById('statusOk');
const errorLabel = document.getElementById('statusError');

const filesList = document.getElementById('filesList');
const scriptsList = document.getElementById('scriptsList');

//ERRORES
const errorMessages = {
    noScript: "ERROR : Script necesario en el area de EA para ejecutar operacion",
    noDocument: "ERROR : Documento necesario en el area de DA para ejecutar operacion",
};

// Cambiar status a OK
function changeStatusOk(){
    okLabel.style.display = "inline";
    errorLabel.style.display = "none";
}

//Cambiar status a error y mostrar error
function changeStatusError(errorMsg){
    okLabel.style.display = "none";
    errorLabel.style.display = "inline";
    errorLabel.innerHTML = errorMsg;
}


//Traer documento XML desde el servidor 
function getExampleDocument(filename) {
    console.log(filename);
    const url = '/document?id=' + filename;

    fetch(url)
    .then(response => response.text())
    .then(data => {
        console.log(data);
        documentArea.value = data;
    })
    .catch(err => console.log(err));
}

//Traer script de Urquery desde el servidor
function getExampleScript(scriptName) {
    console.log(scriptName);
    const url = '/script?id=' + scriptName;

    fetch(url)
    .then(response => response.text())
    .then(data => {
        console.log(data);
        editionArea.value = data;
    })
    .catch(err => console.log(err));
}

//Compilar EA
function compile() {
    let scriptTxt = editionArea.value;

    //No script
    if(scriptTxt === ""){
     changeStatusError(errorMessages.noScript);
     return;
 }

    //Cambiar status a OK
    changeStatusOk();

    const url = '/compile';
    const data = { script: scriptTxt};

    fetch(url, {
        method: 'POST',
        headers: {
            'Accept': 'text/plain',
            'Content-Type': 'text/plain'
        },
        body: JSON.stringify(data),
    })
    .then(response => response.json())
    .then(data => {
        console.log(data);

        //status false
        if(!data.status){
            resultArea.value = data.msg;
            return;
        }
        //status true
        resultArea.value = data.result;
    })
    .catch(err => console.log(err));
}

//ejecutar script junto con el documento, y mostrar resultado en RA
function runDocumentWithScript(){
    let doc = documentArea.value;

    //No documento
    if(doc === ""){
        changeStatusError(errorMessages.noDocument);
        return;
    }

     //Cambiar status a OK
     changeStatusOk();
 }

//mostrar contenido de about.json en pantalla
function showAbout() {
    const url = '/about';

    fetch(url)
    .then(response => response.json())
    .then(data => {
        console.log(data);
        fillAbout(data);
    })
    .catch(err => console.log(err));
}

//Llenar popUp con info del json about
function fillAbout(data) {
    let myModal = new bootstrap.Modal(popUpAbout, { keyboard: false });
    myModal.toggle();

    let text = "";
    text += "<b>Grupo: </b>" + data.Grupo + "</br></br>";
    text += "<b>Nombre del curso: </b>" + data.Curso.Nombre + "</br></br>";
    text += "<b>NRC: </b>" + data.Curso.NRC + "</br></br>";
    text += "<b>Escuela: </b>" + data.Universidad + "</br></br>";
    text += "<b>Ciclo: </b>" + data.Ciclo + "</br></br>";
    text += "<b>Year: </b>" + data.Year + "</br></br>";
    text += "<b>Integrantes: </b></br></br>";
    for (let i = 0; i < data.Integrantes.length; i++) {
        text += "<b>ID: </b>" + data.Integrantes[i].ID;
        text += "<b> Nombre: </b>" + data.Integrantes[i].Nombre;
        text += "<b> Apellidos: </b>" + data.Integrantes[i].Apellidos + "</br></br>";
    }
    aboutBody.innerHTML = text;
}

// Traer nombres de los documentos XML desde el servidor
function getDocuments() {
   const url = '/listFiles';

   fetch(url)
   .then(response => response.json())
   .then(data => {
    console.log(data);
    listDocuments(data);
})
   .catch(err => console.log(err));
}

// Traer nombres de los scripts de Urquery desde el servidor
function getScripts() {
    const url = '/listScripts';

    fetch(url)
    .then(response => response.json())
    .then(data => {
     console.log(data);
     listScripts(data);
 })
    .catch(err => console.log(err));
}



//Llenar dropdown con los nombres de los documentos XML
function listDocuments(files){
    for(const key in files){

        const fileName = files[key].split(".")[0];

        //Crear a tag
        let a = document.createElement("a");
        a.classList.add("dropdown-item");
        a.setAttribute("href","javascript:void(0)");
        a.innerHTML = fileName;

        //Crear li tag
        let li = document.createElement("li");
        li.addEventListener("click",() => { getExampleDocument(files[key])});
        li.appendChild(a);

        //Annadir li
        filesList.appendChild(li);
    }
}

//Llenar dropdown con los nombres de los scripts de Urquery
function listScripts(scripts){
    for(const key in scripts){

        const scriptName = scripts[key].split(".")[0];

        //Crear a tag
        let a = document.createElement("a");
        a.classList.add("dropdown-item");
        a.setAttribute("href","javascript:void(0)");
        a.innerHTML = scriptName;

        //Crear li tag
        let li = document.createElement("li");
        li.addEventListener("click",() => { getExampleScript(scripts[key])});
        li.appendChild(a);

        //Annadir li
        scriptsList.appendChild(li);
    }
}

//guardar archivo de script en el server
function saveScript(){

    let scriptTxt = editionArea.value;

    //No script
    if(scriptTxt  === ""){
     changeStatusError(errorMessages.noScript);
     return;
    }

    //Cambiar status a OK
    changeStatusOk();

    //File name
    let name = prompt("Nombre del archivo:");

    if (name == null) {
    return;
    }

    const url = "/script";
    const data = { script: scriptTxt, scriptName: name};

    fetch(url, {
        method: 'POST',
        headers: {
            'Accept': 'text/plain',
            'Content-Type': 'text/plain'
        },
        // body: script,
        body: JSON.stringify(data),
    })
    .then(response => response.text())
    .then(data => {
        console.log(data);

        //Annadir nuevo script al dropdown
         //Crear a tag
        let a = document.createElement("a");
        a.classList.add("dropdown-item");
        a.setAttribute("href","javascript:void(0)");
        a.innerHTML = data;

        //Crear li tag
        let li = document.createElement("li");
        li.addEventListener("click",() => { getExampleScript(data + ".txt")});
        li.appendChild(a);

        //Annadir li
        scriptsList.appendChild(li);
    })
    .catch(err => console.log(err));
}

//guardar archivo xml en el server
function saveXmlFile(){

    let file = documentArea.value;

    //No script
    if(file  === ""){
     changeStatusError(errorMessages.noDocument);
     return;
    }

    //Cambiar status a OK
    changeStatusOk();

    //File name
    let name = prompt("Nombre del archivo:");

    if (name == null) {
    return;
    }

    const url = "/document";
    const data = { fileTxt: file, fileName: name};

    console.log(data);

    fetch(url, {
        method: 'POST',
        headers: {
            'Accept': 'text/plain',
            'Content-Type': 'text/plain'
        },
        body: JSON.stringify(data),
    })
    .then(response => response.text())
    .then(data => {
        console.log(data);

        //Annadir nuevo xml al dropdown
         //Crear a tag
        let a = document.createElement("a");
        a.classList.add("dropdown-item");
        a.setAttribute("href","javascript:void(0)");
        a.innerHTML = data;

        //Crear li tag
        let li = document.createElement("li");
        li.addEventListener("click",() => { getExampleDocument(data + ".xml")});
        li.appendChild(a);

        //Annadir li
        filesList.appendChild(li);
    })
    .catch(err => console.log(err));
}

//Permitir tabulador en textarea
function enableTab(e,textarea) {
     if (e.keyCode === 9) {
    e.preventDefault()

    textarea.setRangeText(
      '  ',
      textarea.selectionStart,
      textarea.selectionStart,
      'end'
    )
  }
}

//Asociar eventos
window.addEventListener("load",getDocuments);
window.addEventListener("load",getScripts);

document.getElementById("compileBtn").addEventListener("click", compile);
document.getElementById("runBtn").addEventListener("click", runDocumentWithScript);
document.getElementById("aboutBtn").addEventListener("click", showAbout);

//Botones de limpiar
document.getElementById("btnCleanEA").addEventListener("click", ()=>{editionArea.value = ""});
document.getElementById("btnCleanDA").addEventListener("click", ()=>{documentArea.value = ""});

//Botones de guardar
document.getElementById("btnSaveScript").addEventListener("click", saveScript);
document.getElementById("btnSaveFile").addEventListener("click", saveXmlFile);

//Habilitar tab en areas de texto
editionArea.addEventListener('keydown', (e) => {enableTab(e,editionArea)});
documentArea.addEventListener('keydown', (e) => {enableTab(e,documentArea)});