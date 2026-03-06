//Konstanten
const button = document.getElementById("uploadButton");
const input = document.getElementById("fileInput");
const table = document.querySelector(".filetable tbody");

//Eventlistener hinzufügen
button.addEventListener("click", () => {
  input.click();
});

input.addEventListener("change", uploadFile);

document.querySelector(".filetable").addEventListener("click", (event) => {
  if (event.target.classList.contains("menu-button")) {
    event.stopPropagation();

    const row = event.target.closest("tr");
    const menu = row.querySelector(".dropdown");

    menu.classList.toggle("show");
  }
});

// Menü schließen wenn außerhalb geklickt wird
document.addEventListener("click", () => {
  document.querySelectorAll(".dropdown").forEach((menu) => {
    menu.classList.remove("show");
  });
});

document
  .querySelector(".filetable")
  .addEventListener("click", async (event) => {
    if (event.target.id === "loeschenItem") {
      const row = event.target.closest("tr");
      const id = row.dataset.id;

      try {
        const response = await fetch("/dashboard/delete/" + id, {
          method: "DELETE",
        });

        if (response.ok) {
          row.remove(); // direkt aus Tabelle entfernen
        } else {
          alert("Löschen fehlgeschlagen");
        }
      } catch (error) {
        alert("Serverfehler");
      }
    }
  });

//Funktionen

async function uploadFile() {
  const file = input.files[0];

  const formData = new FormData();
  formData.append("file", file);

  try {
    const response = await fetch("/dashboard/upload", {
      method: "POST",
      body: formData,
    });

    if (response.ok) {
      input.value = ""; // Reset
      const json = await response.json();
      addRow(json);
    } else {
      console.log("Upload fehlgeschlagen");
    }
  } catch (error) {
    console.log("Serverfehler");
  }
}

async function uploadDurchButton() {
  input.click();
  uploadFile();
}

function addRow(file) {
  const row = table.insertRow();
  row.dataset.id = file.fileid;

  const cell1 = row.insertCell();
  const cell2 = row.insertCell();
  const cell3 = row.insertCell();
  const cell4 = row.insertCell();
  const cell5 = row.insertCell();

  // Link sauber erstellen
  const link = document.createElement("a");
  link.href = `/dashboard/download/${file.fileid}`;
  link.textContent = file.originalFileName;

  cell1.appendChild(link);

  cell2.textContent = file.uploadTime;
  cell3.textContent = file.sizeForVisuell;
  cell4.textContent = file.contentType;

  cell5.innerHTML = `
   <div class="file-row">
              <button
                class="menu-button"
                aria-haspopup="true"
                aria-expanded="false"
              >
                ⋮
              </button>

              <ul class="dropdown" role="menu">
                <li role="menuitem">
                  <button id="loeschenItem">Löschen</button>
                </li>
                 <li role="menuitem">
                  <button id="umbenennenItem">Umbenennen</button>
                </li>
              </ul>
            </div>
  `;
}
