import React, { useState } from 'react';

import "./ModificarProducto.css";

function ModificarProducto({ api }) {
    const [producto, setProducto] = useState({
        id: 3,  
        codigo: '',
        descripcion: '',
        precio: '',
        marca: '',
        categoria: '',
    });
    const [mensajeError, setMensajeError] = useState('');

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        console.log(`Campo: ${name}, Valor: ${value}`); 
    
        setProducto({
            ...producto,
            [name]: value
        });
    };
     const handleSubmit = (e) => {
        e.preventDefault();
        fetch(`${api}/api/productos/modificar/${producto.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                id: producto.id,
                codigo: producto.codigo,
                descripcion: producto.descripcion,
                precio: parseFloat(producto.precio),
                marca: producto.marca,  
                categoria: producto.categoria,
            }), 
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error de concurrencia');
                }
            })
            .then(data => {
                alert('Producto modificado exitosamente');
            })
            .catch(error => {
                setMensajeError('El producto ha sido modificado por otro usuario. Por favor, recarga la página.' + error);
            });
    };

    return (
        <form onSubmit={handleSubmit}>
            <label>ID: {producto.id}</label><br />
            <input
                type="text"
                name="descripcion"
                value={producto.descripcion}
                onChange={handleInputChange}
                placeholder="Descripcion del producto"
            /><br />
            <input
                type="text"
                name="codigo"
                value={producto.codigo}
                onChange={handleInputChange}
                placeholder="Codigo del producto"
            /><br />
            <input
                type="number"
                name="precio"
                value={producto.precio}
                onChange={handleInputChange}
                placeholder="Precio del producto"
            /><br />
            <input
                type="text"
                name="marca"
                value={producto.marca}
                onChange={handleInputChange}
                placeholder="Marca del producto"
            /><br />
            <select
                name="categoria"
                value={producto.categoria}
                onChange={handleInputChange}
            >
                <option value="">Seleccione una categoría</option> 
                <option value="Pantalon">Pantalon</option>
                <option value="Remera">Remera</option>
                <option value="Vestido">Vestido</option>
            </select><br />
            {mensajeError && <p>{mensajeError}</p>}
            <button type="submit">Modificar Producto</button>
        </form>
    );
}

export default ModificarProducto;
