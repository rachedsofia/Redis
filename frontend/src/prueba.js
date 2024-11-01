import { useState } from "react";

function prueba() {
  // eslint-disable-next-line react-hooks/rules-of-hooks
  const [characters, setCharacters] = useState(null);

  const reqApi = async () => {
    const api = await fetch("https://localhost:8001/api/productos");

    const charactersApi = await api.json();
    console.log(charactersApi);

  };

  return (
    <div className="App">
      <header className="App-header">
        <h1 className="title">Rick & Morty </h1>

            <button onClick={reqApi} className="btn-search">
              Buscar personajes
            </button>
      </header>
    </div>
  );
}

export default prueba;