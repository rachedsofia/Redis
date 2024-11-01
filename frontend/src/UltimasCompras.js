import React, { useEffect, useState } from 'react';
import './UltimasCompras.css';

function UltimasCompras({ api, idCliente }) {
  const [compras, setCompras] = useState([]);
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(true); // Estado de carga

  useEffect(() => {
    const fetchUltimasCompras = async () => {
      setIsLoading(true); // Mostrar indicador de carga
      try {
        const response = await fetch(`${api}/api/compras/ultimas/${idCliente}`);
        if (!response.ok) {
          throw new Error('Error al obtener las últimas compras');
        }
        const data = await response.json();
        setCompras(data);
      } catch (err) {
        console.error(err);
        setError('No se pudieron cargar las últimas compras.');
      } finally {
        setIsLoading(false); // Ocultar indicador de carga
      }
    };

    fetchUltimasCompras();
  }, [api, idCliente]);

  return (
    <div className="compras-container">
      <h2 className="titulo">Últimas Compras</h2>
      {isLoading && <p>Cargando compras...</p>}
      {error && <p className="error">{error}</p>}
      {!isLoading && compras.length === 0 && (
        <p className="no-compras">No hay compras recientes</p>
      )}
      {!isLoading && compras.length > 0 && (
        <ul className="compras-lista">

        {compras.map((compra) => (
           <li key={compra.id} className="compra-item">
           <p><strong>Fecha:</strong> {new Date(compra.fechaYHora).toLocaleDateString()}</p>
           <p><strong>Total:</strong> ${compra.montoTotal}</p>
           <p><strong>Productos:</strong> {compra.misProductos.map(prod => `${prod.descripcion} (${prod.miCategoria})`)}.</p>
           </li>
         ))}
        
        </ul>
      )}
    </div>
  );
}

export default UltimasCompras;