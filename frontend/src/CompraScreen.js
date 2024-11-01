import React, { useState, useEffect } from "react";
import {
  Container,
  Typography,
  Button,
  Select,
  MenuItem,
  FormControl,
  List,
  ListItem,
  Snackbar,
  Card,
  CardContent,
  Stack,
} from "@mui/material";
import Modal from '@mui/material/Modal';

import "./CompraScreen.css";
import Box from "@mui/material/Box";

function CompraScreen ({api}) {

  const [productos, setProductos] = useState([]);
  const [descuentos, setDescuentos] = useState([]);
  const [tarjetas, setTarjetas] = useState([]);
  const [selectedProductos, setSelectedProductos] = useState([]);
  const [selectedTarjeta, setSelectedTarjeta] = useState("");
  const [montoTotal, setMontoTotal] = useState(0);
  const [mensajeError, setMensajeError] = useState("");
  const idCliente = 1;
  const [modalIsOpen, setModalIsOpen] = useState(false);
  const [mensaje, setMensaje] = useState('');
  const [modalContent, setModalContent] = useState(""); // Contenido del modal

useEffect(() => {
  fetch(api + "/api/productos")
  .then((response) => {
    if (!response.ok) {
      throw new Error("Error al traer productos");
    }
    return response.json();
  })
  .then((data) => {
    console.log("Productos:", data);
    setProductos(data);
  })
  .catch((error) => console.error("Error al traer productos:", error));

// Obtener descuentos

fetch(api + "/api/descuentos")
  .then((response) => {
    if (!response.ok) {
      throw new Error("Error al traer descuentos");
    }
    return response.json();
  })
  .then((data) => {
    console.log("Descuentos:", data);
    setDescuentos(data);
  })
  .catch((error) => console.error("Error al traer descuentos:", error));

//* Obtener tarjetas del cliente
// eslint-disable-next-line no-template-curly-in-string
fetch(api + "/api/tarjetas/1")
  .then((response) => {
    if (!response.ok) {
      throw new Error("Error al traer tarjetas");
    }
    return response.json();
  })
  .then((data) => {
    console.log("Tarjetas:", data);
    setTarjetas(data);
  })
  .catch((error) => console.error("Error al traer tarjetas:", error));
}, [api, idCliente]);


//CALCULAR TOTAL
const handleCalcularTotal = () => {
  // 1. Calcular el monto total de los productos seleccionados
  let montoTotal = selectedProductos.reduce((acc, productoId) => {
    const producto = productos.find((p) => p.id === productoId);
    return acc + (producto ? producto.precio : 0);
  }, 0);

  // 2. Verificar si hay productos seleccionados
  if (selectedProductos.length === 0) {
    setMensajeError("Debes seleccionar al menos un producto.");
    return;
  }

  // 3. Obtener la tarjeta seleccionada
  const tarjetaSeleccionada = tarjetas.find((tarjeta) => tarjeta.id === selectedTarjeta);
  
  if (!tarjetaSeleccionada) {
    setMensajeError("Debes seleccionar una tarjeta.");
    return;
  }

  console.log("Productos seleccionados:", selectedProductos);
  console.log("ID Tarjeta:", tarjetaSeleccionada.id);
  console.log("Monto total calculado localmente:", montoTotal);

  fetch(api + "/api/ventas/calcular-monto", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      productos: selectedProductos,
      idTarjeta: tarjetaSeleccionada.id,
    }),
  })
  .then(response => {
    if (!response.ok) {
      throw new Error("Error en la respuesta del servidor");
    }
    return response.json();
  })
  .then(data => {
    console.log("Monto calculado desde el servidor:", data);
    setMontoTotal(data); // Guarda el monto en el estado
  })
  .catch(error => {
    console.error("Error al calcular el monto:", error);
    setMensajeError("Ocurrió un error al calcular el monto. Inténtalo de nuevo.");
  });
};

 // Realizar la compra
  const handleCompra = async () => {
    const tarjetaSeleccionada = tarjetas.find((tarjeta) => tarjeta.id === selectedTarjeta);
    console.log("Tarjeta : " + tarjetaSeleccionada.id);
    console.log("Cliente: " + idCliente);
    console.log("Productos: " + selectedProductos);

    try {
        const response = await fetch(api + '/api/ventas/realizar-compra', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                idCliente: idCliente,
                productos: selectedProductos,
                idTarjeta: tarjetaSeleccionada.id,
            }),
        });

        if (response.ok) {
          setModalIsOpen(true); 
          setModalContent('Compra realizada con éxito'); 
          console.log(response.ok);
          console.log("hola");
          setMensaje('Compra realizada con éxito');
           
        } else {
            const errorData = await response.json(); // Esperar el JSON
            setMensaje(`Error: ${errorData.message}`);
            setModalIsOpen(true);
        }
    } catch (error) {
        setMensaje(`Error de red: ${error.message}`);
        setModalIsOpen(true);
    }
};

// Función para cerrar el modal
const handleCloseModal = () => {
  setModalIsOpen(false);
  setMensaje(""); // Limpiar mensaje al cerrar
};

  return (
    <Container maxWidth="md" className="container">
      <Typography variant="h4" className="title">
        Compra de Productos
      </Typography><br></br>
      {/* Lista de productos */}
      <Card className="card">
        <CardContent>
          <Typography variant="h6" className="card-title">
            Selecciona productos
          </Typography>
          <FormControl fullWidth variant="outlined">
            <Select
              multiple
              value={selectedProductos}
              onChange={(e) => setSelectedProductos(e.target.value)}
              renderValue={(selected) =>
                selected
                  .map((id) => productos.find((p) => p.id === id)?.nombre)
                  .join(", ")
              }
             // inputProps={{ "aria-label": "Seleccionar productos" }}
              className="select"
            >
              {productos.map((producto) => (
                <MenuItem key={producto.id} value={producto.id}>
                  {producto.descripcion} - ${producto.precio}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </CardContent>
      </Card>
      <ul>
      {selectedProductos.map((productId) => {
        const productoSeleccionado = productos.find((p) => p.id === productId);
        return (
          <li key={productId}>
            {productoSeleccionado.descripcion} - ${productoSeleccionado.precio}
          </li>
        );
      })}
    </ul>
      {/* Lista de descuentos */}
      <Card className="card">
        <CardContent>
          <Typography variant="h6" className="card-title">
            Descuentos vigentes
          </Typography>
         <List>
            {descuentos.map((descuento) => (
              <ListItem key={descuento.id} className="list-item">
                {descuento.marca} {descuento.medioDePago} - {descuento.porcentaje}% de descuento
              </ListItem>
            ))}
          </List>
        </CardContent>
      </Card>
      {/* Lista de tarjetas */}
      <Card className="card">
        <CardContent>
          <Typography variant="h6" className="card-title">
            Selecciona una tarjeta de crédito
          </Typography>
          <FormControl fullWidth variant="outlined">
          
            <Select
              value={selectedTarjeta}
              onChange={(e) => setSelectedTarjeta(e.target.value)}
             // inputProps={{ "aria-label": "Seleccionar tarjeta" }}
              className="select"
            >
              {tarjetas.map((tarjeta) => (
                <MenuItem key={tarjeta.id} value={tarjeta.id}>
                  {tarjeta.codigo1} - {tarjeta.marca}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </CardContent>
        
      </Card>
      {/* Botón para calcular total */}
      <Stack
        container
        direction="row"
        justifyContent="space-between"
        alignItems="center"
        sx={{ mb: 4 }}
      >
        <Button
          variant="contained"
          color="primary"
          onClick={handleCalcularTotal}
          className="calculate-button"
        >
          Calcular Precio Total
        </Button>
        <Typography variant="h5" className="total-amount">
          Total: ${montoTotal}
        </Typography>
      </Stack>
      {/* Botón para realizar compra */}
      <Button
        variant="contained"
        color="secondary"
        onClick={handleCompra}
        className="purchase-button"
      >
        Realizar Compra
      </Button>
      {/* Mostrar errores */}
      {mensajeError && (
        <Snackbar
          open={true}
          autoHideDuration={6000}
          message={mensajeError}
          onClose={() => setMensajeError("")}
          className="snackbar"
        />
      )}
 <Modal
        open={modalIsOpen}
        onClose={handleCloseModal}
        aria-labelledby="modal-title"
        aria-describedby="modal-description"
      >
        <Box className="modal">
          <div className="modal-content">
            <Typography id="modal-title" variant="h6" className="modal-title">
              Éxito
            </Typography>
            <Typography id="modal-description" className="modal-description">
              {mensaje}
            </Typography>
            <Button onClick={handleCloseModal} className="modal-button">
              Cerrar
            </Button>
          </div>
        </Box>
      </Modal>
    </Container>
  );
  
};

export default CompraScreen;
