
//CONSTANTES
const editionArea = document.getElementById("EA");
const documentArea = document.getElementById("DA");
const resultArea = document.getElementById("RA");

const popUpAbout = document.getElementById('popUpAbout');
const aboutBody = document.getElementById('aboutBody');

const ok = document.getElementById('statusOk');
const error = document.getElementById('statusError');

//Traer documento desde el servidor 
function getExampleDocument(filename) {
    console.log(filename);
    const url = 'http://localhost:8081/urquery/document?id=' + filename;
    console.log(url);

    fetch(url)
        .then(response => response.text())
        .then(data => {
            console.log(data);
            documentArea.value = data;
        })
        .catch(err => console.log(err));

    const url2 = "http://localhost:8082/test";
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
    const url = 'http://localhost:8081/urquery/compile?EA=' + text;
    console.log(url);

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
    const url = 'http://localhost:8081/urquery/about';
    console.log(url);

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

//Asociar eventos
document.getElementById("test1").addEventListener("click", () => { getExampleDocument('test1.txt') });
document.getElementById("test2").addEventListener("click", () => { getExampleDocument('test2.txt') });
document.getElementById("test3").addEventListener("click", () => { getExampleDocument('test3.txt') });
document.getElementById("runBtn").addEventListener("click", compile);
document.getElementById("aboutBtn").addEventListener("click", showAbout);




