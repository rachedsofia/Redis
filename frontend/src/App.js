import "./App.css";
import CompraScreen from "./CompraScreen";
import ModificarProducto from "./ModificarProducto";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import UltimasCompras from "./UltimasCompras";

function App() {
 /* const [characters, setCharacters] = useState(null);

  const reqApi = async () => {
    const api = await fetch("http://localhost:8081/api/descuentos");

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
  );*/
  const api = process.env.REACT_APP_API_GW;
  const idCliente = 1;  
  return (
    <BrowserRouter>
      <Routes>
        <Route
          path="/"
        /* TP REDIS */ element={<UltimasCompras api ={api} idCliente={idCliente} />} 
         /* TP CONCURRENCIA  element={<ModificarProducto api={api} />} */
         // element={<CompraScreen api={api} />}
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
