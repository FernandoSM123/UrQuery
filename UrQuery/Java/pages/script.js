
//CONSTANTES
const editionArea = document.getElementById("EA");
const documentArea = document.getElementById("DA");
const resultArea = document.getElementById("RA");

const popUpAbout = document.getElementById('popUpAbout');
const aboutBody = document.getElementById('aboutBody');

const ok = document.getElementById('statusOk');
const error = document.getElementById('statusError');

const filesList = document.getElementById('filesList');

//Traer documento desde el servidor 
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

//Compilar EA
function compile() {
    let text = editionArea.value;

    //Texto vacio
    if(text === ""){
        ok.style.display = "none";
        error.style.display = "inline";
        resultArea.value = "";
        return;
    }


    ok.style.display = "inline";
    error.style.display = "none";
    const url = '/compile?EA=' + text;

    fetch(url, {
        method: 'POST',
        headers: {
            'Accept': 'text/plain',
            'Content-Type': 'text/plain'
        },
    })
        .then(response => response.text())
        .then(data => {
            console.log(data);
            resultArea.value = data;
        })
        .catch(err => console.log(err));
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

// Traer archivos de ejemplo desde el servidor
function getTestFiles() {
     const url = '/listFiles';

    fetch(url)
        .then(response => response.json())
        .then(data => {
            console.log(data);
            listTestFiles(data);
        })
        .catch(err => console.log(err));
}

//Listar documentos de prueba en la pagina
function listTestFiles(files){
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

//Asociar eventos
window.onload = getTestFiles;
document.getElementById("compileBtn").addEventListener("click", compile);
document.getElementById("aboutBtn").addEventListener("click", showAbout);