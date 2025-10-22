import React, { useState, useEffect } from 'react';
import { AlertCircle, Users, Package, LogOut, Menu, X, Calendar, FileText, BarChart3, Edit2, Trash2, Save, CheckCircle, XCircle, Clock, Eye } from 'lucide-react';

// Configuración de la API
const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

const ESTADOS_CITA = {
  PENDIENTE: { label: 'Pendiente', color: 'bg-yellow-100 text-yellow-800', icon: Clock },
  CONFIRMADA: { label: 'Confirmada', color: 'bg-blue-100 text-blue-800', icon: CheckCircle },
  ATENDIDA: { label: 'Atendida', color: 'bg-green-100 text-green-800', icon: CheckCircle },
  CANCELADA: { label: 'Cancelada', color: 'bg-red-100 text-red-800', icon: XCircle },
  RECHAZADA: { label: 'Rechazada', color: 'bg-gray-100 text-gray-800', icon: XCircle }
};


// COMPONENTE DE LOGIN (UC-02)
const Login = ({ onLogin }) => {
  const [credentials, setCredentials] = useState({ username: '', password: '' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const validateForm = () => {
    if (!credentials.username.trim()) {
      setError('El usuario es requerido');
      return false;
    }
    if (!credentials.password.trim()) {
      setError('La contraseña es requerida');
      return false;
    }
    if (credentials.password.length < 6) {
      setError('La contraseña debe tener al menos 6 caracteres');
      return false;
    }
    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    
    if (!validateForm()) return;

    setLoading(true);
    
    try {
      const response = await fetch(`${API_URL}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(credentials)
      });

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || 'Credenciales inválidas');
      }
      
      const data = await response.json();
    console.log('=== RESPONSE LOGIN ===');
    console.log('Data completa:', data);
    console.log('Token:', data.token);
    console.log('Usuario:', data.username);
  onLogin(data.token, data.username || credentials.username);
    } catch (err) {
      setError(err.message || 'Error al iniciar sesión. Verifique sus credenciales.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-500 via-purple-500 to-pink-500 flex items-center justify-center p-4">
      <div className="bg-white rounded-2xl shadow-2xl w-full max-w-md p-8 transform transition-all">
        <div className="text-center mb-8">
          <div className="mx-auto w-16 h-16 bg-gradient-to-r from-blue-500 to-purple-600 rounded-full flex items-center justify-center mb-4">
            <Users className="text-white" size={32} />
          </div>
          <h1 className="text-3xl font-bold text-gray-800 mb-2">Bienvenido</h1>
          <p className="text-gray-600">Sistema de Gestión de Bienestar</p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Usuario *
            </label>
            <input
              type="text"
              value={credentials.username}
              onChange={(e) => setCredentials({...credentials, username: e.target.value})}
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition"
              placeholder="Ingresa tu usuario"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Contraseña *
            </label>
            <input
              type="password"
              value={credentials.password}
              onChange={(e) => setCredentials({...credentials, password: e.target.value})}
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition"
              placeholder="Ingresa tu contraseña"
            />
          </div>

          {error && (
            <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg flex items-center gap-2">
              <AlertCircle size={20} />
              <span className="text-sm">{error}</span>
            </div>
          )}

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white font-semibold py-3 rounded-lg transition disabled:opacity-50 disabled:cursor-not-allowed transform hover:scale-105"
          >
            {loading ? 'Iniciando sesión...' : 'Iniciar Sesión'}
          </button>
        </form>

        <div className="mt-6 text-center text-sm text-gray-600">
          <p className="bg-blue-50 p-3 rounded-lg">
            <strong>Prueba:</strong> admin / admin123
          </p>
        </div>
      </div>
    </div>
  );
};
 // ============================================
// COMPONENTE DE GESTIÓN DE CLIENTES 
// ============================================
const Clientes = ({ token }) => {
  const [clientes, setClientes] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [formData, setFormData] = useState({
    nombre: '',
    email: '',
    telefono: '',
    direccion: '',
    dpi: '',
    fechaNacimiento: ''
  });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
  if (token) {
    fetchClientes();
  }
}, [token]);

  const fetchClientes = async () => {
    setLoading(true);
    try {
      const response = await fetch(`${API_URL}/clientes`, {
        headers: { 'Authorization': `Bearer ${token}` }
      });
      if (response.ok) {
        const data = await response.json();
        console.log('=== CLIENTES OBTENIDOS ===', data);
        // Mapear los datos si vienen con nombres diferentes
        const clientesMapeados = data.map(cliente => ({
          id: cliente.id,
          nombre: cliente.nombreCompleto || cliente.nombre,
          email: cliente.email || cliente.username,
          telefono: cliente.telefono,
          direccion: cliente.direccion,
          dpi: cliente.dpi,
          fechaNacimiento: cliente.fechaNacimiento
        }));
        setClientes(clientesMapeados);
      } else {
        console.error('Error en respuesta:', response.status);
      }
    } catch (err) {
      console.error('Error al obtener clientes:', err);
    } finally {
      setLoading(false);
    }
  };

  const validateForm = () => {
    const newErrors = {};
    
    if (!formData.nombre.trim()) {
      newErrors.nombre = 'El nombre es requerido';
    } else if (formData.nombre.length < 3) {
      newErrors.nombre = 'El nombre debe tener al menos 3 caracteres';
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!formData.email.trim()) {
      newErrors.email = 'El email es requerido';
    } else if (!emailRegex.test(formData.email)) {
      newErrors.email = 'Email inválido';
    }

    const phoneRegex = /^\d{8,}$/;
    if (!formData.telefono.trim()) {
      newErrors.telefono = 'El teléfono es requerido';
    } else if (!phoneRegex.test(formData.telefono.replace(/[\s-]/g, ''))) {
      newErrors.telefono = 'Teléfono inválido (mínimo 8 dígitos)';
    }

    if (!formData.direccion.trim()) {
      newErrors.direccion = 'La dirección es requerida';
    }

    if (formData.dpi && formData.dpi.length < 8) {
      newErrors.dpi = 'El DPI debe tener al menos 8 dígitos';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) return;
    setLoading(true);

    const url = editingId 
      ? `${API_URL}/clientes/${editingId}` 
      : `${API_URL}/clientes/registro`;
    
    const method = editingId ? 'PUT' : 'POST';
    const payload = {
      username: formData.email.split('@')[0],
      password: '12345678',
      email: formData.email.trim(),
      nombreCompleto: formData.nombre.trim(),
      dpi: formData.dpi.trim(),
      telefono: formData.telefono.trim(),
      direccion: formData.direccion.trim(),
      fechaNacimiento: formData.fechaNacimiento || '2000-01-01'
    };

    console.log('=== DEBUG ClienteDTO enviado ===');
    console.log(JSON.stringify(payload, null, 2));

    try {
      const response = await fetch(url, {
        method,
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(payload)
      });

      if (response.ok) {
        const respuestaData = await response.json();
        console.log('=== RESPUESTA DEL SERVIDOR ===', respuestaData);
        
        // Esperar un pequeño delay y luego refrescar
        setTimeout(() => {
          fetchClientes();
        }, 500);
        
        resetForm();
        alert(editingId ? 'Cliente actualizado exitosamente' : 'Cliente registrado exitosamente');
      } else {
        const error = await response.json();
        console.error('Error respuesta:', error);
        alert(error.message || 'Error al guardar cliente');
      }
    } catch (err) {
      console.error('Error al guardar cliente:', err);
      alert('Error al guardar cliente. Intente nuevamente.');
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (cliente) => {
    setFormData({
      nombre: cliente.nombre,
      email: cliente.email,
      telefono: cliente.telefono,
      direccion: cliente.direccion,
      dpi: cliente.dpi || '',
      fechaNacimiento: cliente.fechaNacimiento || ''
    });
    setEditingId(cliente.id);
    setShowForm(true);
    setErrors({});
  };

  const handleDelete = async (id) => {
    if (!window.confirm('¿Está seguro de desactivar este cliente?')) return;

    setLoading(true);
    try {
      const response = await fetch(`${API_URL}/clientes/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${token}` }
      });

      if (response.ok) {
        await fetchClientes();
        alert('Cliente desactivado exitosamente');
      } else {
        alert('Error al desactivar cliente');
      }
    } catch (err) {
      console.error('Error al eliminar cliente:', err);
      alert('Error al desactivar cliente');
    } finally {
      setLoading(false);
    }
  };

  const resetForm = () => {
    setFormData({ nombre: '', email: '', telefono: '', direccion: '', dpi: '', fechaNacimiento: '' });
    setEditingId(null);
    setShowForm(false);
    setErrors({});
  };

  const filteredClientes = clientes.filter(cliente =>
    cliente.nombre.toLowerCase().includes(searchTerm.toLowerCase()) ||
    cliente.email.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h2 className="text-2xl font-bold text-gray-800">Gestión de Clientes</h2>
          <p className="text-gray-600 text-sm">UC-01, UC-07, UC-10, UC-11</p>
        </div>
        <button
          onClick={() => setShowForm(!showForm)}
          className="bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white px-6 py-2 rounded-lg font-medium transition transform hover:scale-105"
        >
          {showForm ? 'Cancelar' : '+ Nuevo Cliente'}
        </button>
      </div>

      {showForm && (
        <div className="bg-white rounded-lg shadow-lg p-6 border-l-4 border-blue-500">
          <h3 className="text-xl font-semibold mb-4 text-gray-800">
            {editingId ? 'Editar Cliente' : 'Nuevo Cliente'}
          </h3>
          <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Nombre Completo *
              </label>
              <input
                type="text"
                value={formData.nombre}
                onChange={(e) => setFormData({...formData, nombre: e.target.value})}
                className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 ${
                  errors.nombre ? 'border-red-500' : 'border-gray-300'
                }`}
                placeholder="Juan Pérez"
              />
              {errors.nombre && (
                <p className="text-red-500 text-xs mt-1">{errors.nombre}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Email *
              </label>
              <input
                type="email"
                value={formData.email}
                onChange={(e) => setFormData({...formData, email: e.target.value})}
                className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 ${
                  errors.email ? 'border-red-500' : 'border-gray-300'
                }`}
                placeholder="juan@ejemplo.com"
              />
              {errors.email && (
                <p className="text-red-500 text-xs mt-1">{errors.email}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Teléfono *
              </label>
              <input
                type="tel"
                value={formData.telefono}
                onChange={(e) => setFormData({...formData, telefono: e.target.value})}
                className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 ${
                  errors.telefono ? 'border-red-500' : 'border-gray-300'
                }`}
                placeholder="12345678"
              />
              {errors.telefono && (
                <p className="text-red-500 text-xs mt-1">{errors.telefono}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                DPI
              </label>
              <input
                type="text"
                value={formData.dpi}
                onChange={(e) => setFormData({...formData, dpi: e.target.value})}
                className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 ${
                  errors.dpi ? 'border-red-500' : 'border-gray-300'
                }`}
                placeholder="12345678901234"
              />
              {errors.dpi && (
                <p className="text-red-500 text-xs mt-1">{errors.dpi}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Fecha de Nacimiento
              </label>
              <input
                type="date"
                value={formData.fechaNacimiento}
                onChange={(e) => setFormData({...formData, fechaNacimiento: e.target.value})}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Dirección *
              </label>
              <input
                type="text"
                value={formData.direccion}
                onChange={(e) => setFormData({...formData, direccion: e.target.value})}
                className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 ${
                  errors.direccion ? 'border-red-500' : 'border-gray-300'
                }`}
                placeholder="Calle Principal, Zona 1"
              />
              {errors.direccion && (
                <p className="text-red-500 text-xs mt-1">{errors.direccion}</p>
              )}
            </div>

            <div className="md:col-span-2 flex gap-3">
              <button
                type="submit"
                disabled={loading}
                className="bg-green-600 hover:bg-green-700 text-white px-6 py-2 rounded-lg font-medium transition flex items-center gap-2 disabled:opacity-50"
              >
                <Save size={18} />
                {editingId ? 'Actualizar' : 'Guardar'}
              </button>
              <button
                type="button"
                onClick={resetForm}
                className="bg-gray-500 hover:bg-gray-600 text-white px-6 py-2 rounded-lg font-medium transition"
              >
                Cancelar
              </button>
            </div>
          </form>
        </div>
      )}

      <div className="bg-white rounded-lg shadow-lg">
        <div className="p-4 border-b">
          <input
            type="text"
            placeholder="Buscar por nombre o email..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50 border-b">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Nombre</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Email</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Teléfono</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">DPI</th>
                <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase">Acciones</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200">
              {loading ? (
                <tr>
                  <td colSpan="5" className="px-6 py-8 text-center text-gray-500">
                    Cargando clientes...
                  </td>
                </tr>
              ) : filteredClientes.length === 0 ? (
                <tr>
                  <td colSpan="5" className="px-6 py-8 text-center text-gray-500">
                    No hay clientes registrados
                  </td>
                </tr>
              ) : (
                filteredClientes.map((cliente) => (
                  <tr key={cliente.id} className="hover:bg-gray-50 transition">
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{cliente.nombre}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600">{cliente.email}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600">{cliente.telefono}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600">{cliente.dpi || 'N/A'}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium space-x-3">
                      <button
                        onClick={() => handleEdit(cliente)}
                        className="text-blue-600 hover:text-blue-900 inline-flex items-center gap-1"
                      >
                        <Edit2 size={16} />
                        Editar
                      </button>
                      <button
                        onClick={() => handleDelete(cliente.id)}
                        className="text-red-600 hover:text-red-900 inline-flex items-center gap-1"
                      >
                        <Trash2 size={16} />
                        Desactivar
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

// ============================================
// COMPONENTE DE CITAS (UC-03, UC-04, UC-05)
// ============================================
const Citas = ({ token }) => {
  const [citas, setCitas] = useState([]);
  const [solicitudes, setSolicitudes] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [activeTab, setActiveTab] = useState('mis-citas');
  const [formData, setFormData] = useState({
    clienteId: '',
    servicioId: '',
    fecha: '',
    hora: '',
    notas: ''
  });
  const [errors, setErrors] = useState({});
  const [clientes, setClientes] = useState([]);
  const [servicios, setServicios] = useState([]);
  const [loading, setLoading] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');

   useEffect(() => {
  if (token) {
    cargarDatos();
  }
}, [token]);

  const cargarDatos = async () => {
    await Promise.all([
      fetchCitas(),
      fetchSolicitudes(),
      fetchClientes(),
      fetchServicios()
    ]);
  };

  const fetchCitas = async () => {
    try {
      const response = await fetch(`${API_URL}/citas/estado/CONFIRMADA`, {
        headers: { 'Authorization': `Bearer ${token}` }
      });
      if (response.ok) {
        const data = await response.json();
        setCitas(data);
      }
    } catch (err) {
      console.error('Error al obtener citas:', err);
    }
  };

  const fetchSolicitudes = async () => {
    try {
      const response = await fetch(`${API_URL}/citas/estado/PENDIENTE`, {
        headers: { 'Authorization': `Bearer ${token}` }
      });
      if (response.ok) {
        const data = await response.json();
        setSolicitudes(data);
      }
    } catch (err) {
      console.error('Error al obtener solicitudes:', err);
    }
  };

  const fetchClientes = async () => {
    try {
      const response = await fetch(`${API_URL}/clientes`, {
        headers: { 'Authorization': `Bearer ${token}` }
      });
      if (response.ok) {
        const data = await response.json();
        setClientes(data);
      }
    } catch (err) {
      console.error('Error al obtener clientes:', err);
    }
  };

  const fetchServicios = async () => {
    try {
      const response = await fetch(`${API_URL}/servicios`, {
        headers: { 'Authorization': `Bearer ${token}` }
      });
      if (response.ok) {
        const data = await response.json();
        setServicios(data);
      }
    } catch (err) {
      console.error('Error al obtener servicios:', err);
    }
  };

  const validarFormulario = () => {
    const newErrors = {};

    if (!formData.clienteId) {
      newErrors.clienteId = 'Seleccione un cliente';
    }

    if (!formData.servicioId) {
      newErrors.servicioId = 'Seleccione un servicio';
    }

    if (!formData.fecha) {
      newErrors.fecha = 'Seleccione una fecha';
    }

    if (!formData.hora) {
      newErrors.hora = 'Seleccione una hora';
    }

    // Validar antelación mínima de 2 horas (UC-03)
    if (formData.fecha && formData.hora) {
      const fechaHora = new Date(`${formData.fecha}T${formData.hora}`);
      const ahora = new Date();
      const diferenciaMilisegundos = fechaHora - ahora;
      const diferenciasHoras = diferenciaMilisegundos / (1000 * 60 * 60);

      if (diferenciasHoras < 2) {
        newErrors.fecha = 'La cita debe ser al menos 2 horas a partir de ahora';
      }
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const crearCita = async (e) => {
    e.preventDefault();

    if (!validarFormulario()) return;

    setLoading(true);

    const payload = {
      clienteId: parseInt(formData.clienteId),
      servicioId: parseInt(formData.servicioId),
      fechaHora: `${formData.fecha}T${formData.hora}:00`,
      notas: formData.notas.trim()
    };

    try {
      const response = await fetch(`${API_URL}/citas`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(payload)
      });

      if (response.ok) {
        alert('Cita agendada exitosamente');
        resetForm();
        await cargarDatos();
      } else {
        const error = await response.json();
        alert(error.message || 'Error al agendar cita');
      }
    } catch (err) {
      console.error('Error al crear cita:', err);
      alert('Error al agendar cita');
    } finally {
      setLoading(false);
    }
  };

  const confirmarSolicitud = async (citaId) => {
    if (!window.confirm('¿Confirmar esta cita?')) return;

    setLoading(true);
    try {
      const response = await fetch(`${API_URL}/citas/${citaId}/confirmar`, {
        method: 'PATCH',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      if (response.ok) {
        alert('Cita confirmada exitosamente');
        await cargarDatos();
      } else {
        alert('Error al confirmar cita');
      }
    } catch (err) {
      console.error('Error:', err);
      alert('Error al confirmar cita');
    } finally {
      setLoading(false);
    }
  };

  const rechazarSolicitud = async (citaId) => {
    const motivo = window.prompt('¿Motivo del rechazo?');
    if (!motivo) return;

    setLoading(true);
    try {
      const response = await fetch(`${API_URL}/citas/${citaId}/rechazar?motivo=${encodeURIComponent(motivo)}`, {
        method: 'PATCH',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      if (response.ok) {
        alert('Cita rechazada');
        await cargarDatos();
      } else {
        alert('Error al rechazar cita');
      }
    } catch (err) {
      console.error('Error:', err);
      alert('Error al rechazar cita');
    } finally {
      setLoading(false);
    }
  };

  const cancelarCita = async (citaId) => {
    // UC-04: Validar que falten al menos 24 horas
    const cita = citas.find(c => c.id === citaId);
    if (cita) {
      const fechaCita = new Date(cita.fechaHora);
      const ahora = new Date();
      const diferenciaMilisegundos = fechaCita - ahora;
      const diferenciasHoras = diferenciaMilisegundos / (1000 * 60 * 60);

      if (diferenciasHoras < 24) {
        alert('No se puede cancelar: debe haber al menos 24 horas de anticipación');
        return;
      }
    }

    if (!window.confirm('¿Cancelar esta cita?')) return;

    setLoading(true);
    try {
      const response = await fetch(`${API_URL}/citas/${citaId}/cancelar`, {
        method: 'PATCH',
        headers: { 'Authorization': `Bearer ${token}` }
      });

      if (response.ok) {
        alert('Cita cancelada exitosamente');
        await cargarDatos();
      } else {
        alert('Error al cancelar cita');
      }
    } catch (err) {
      console.error('Error:', err);
      alert('Error al cancelar cita');
    } finally {
      setLoading(false);
    }
  };

  const resetForm = () => {
    setFormData({
      clienteId: '',
      servicioId: '',
      fecha: '',
      hora: '',
      notas: ''
    });
    setErrors({});
    setShowForm(false);
  };

  const formatearFechaHora = (fechaHora) => {
    try {
      const date = new Date(fechaHora);
      return date.toLocaleString('es-GT', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    } catch {
      return fechaHora;
    }
  };

  const getColorEstado = (estado) => {
    switch (estado) {
      case 'CONFIRMADA':
        return 'bg-green-100 text-green-800 border-green-300';
      case 'PENDIENTE':
        return 'bg-yellow-100 text-yellow-800 border-yellow-300';
      case 'ATENDIDA':
        return 'bg-blue-100 text-blue-800 border-blue-300';
      case 'CANCELADA':
      case 'RECHAZADA':
        return 'bg-red-100 text-red-800 border-red-300';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const filteredCitas = citas.filter(cita =>
    cita.clienteNombre?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const filteredSolicitudes = solicitudes.filter(cita =>
    cita.clienteNombre?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h2 className="text-2xl font-bold text-gray-800">Gestión de Citas</h2>
          <p className="text-gray-600 text-sm">UC-03, UC-04, UC-05: Agendar, Cancelar y Gestionar Solicitudes</p>
        </div>
        <button
          onClick={() => setShowForm(!showForm)}
          className="bg-gradient-to-r from-blue-600 to-cyan-600 hover:from-blue-700 hover:to-cyan-700 text-white px-6 py-2 rounded-lg font-medium transition transform hover:scale-105"
        >
          {showForm ? 'Cancelar' : '+ Nueva Cita'}
        </button>
      </div>

      {/* Formulario Nueva Cita */}
      {showForm && (
        <div className="bg-white rounded-lg shadow-lg p-6 border-l-4 border-blue-500">
          <h3 className="text-xl font-semibold mb-4">Nueva Cita</h3>
          <form onSubmit={crearCita} className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Cliente *</label>
              <select
                value={formData.clienteId}
                onChange={(e) => setFormData({ ...formData, clienteId: e.target.value })}
                className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 ${
                  errors.clienteId ? 'border-red-500' : 'border-gray-300'
                }`}
              >
                <option value="">Seleccionar cliente</option>
                {clientes.map(c => (
                  <option key={c.id} value={c.id}>{c.nombreCompleto}</option>
                ))}
              </select>
              {errors.clienteId && <p className="text-red-500 text-xs mt-1">{errors.clienteId}</p>}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Servicio *</label>
              <select
                value={formData.servicioId}
                onChange={(e) => setFormData({ ...formData, servicioId: e.target.value })}
                className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 ${
                  errors.servicioId ? 'border-red-500' : 'border-gray-300'
                }`}
              >
                <option value="">Seleccionar servicio</option>
                {servicios.map(s => (
                  <option key={s.id} value={s.id}>{s.nombre} - Q {s.precio}</option>
                ))}
              </select>
              {errors.servicioId && <p className="text-red-500 text-xs mt-1">{errors.servicioId}</p>}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Fecha *</label>
              <input
                type="date"
                value={formData.fecha}
                onChange={(e) => setFormData({ ...formData, fecha: e.target.value })}
                min={new Date().toISOString().split('T')[0]}
                className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 ${
                  errors.fecha ? 'border-red-500' : 'border-gray-300'
                }`}
              />
              {errors.fecha && <p className="text-red-500 text-xs mt-1">{errors.fecha}</p>}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Hora *</label>
              <input
                type="time"
                value={formData.hora}
                onChange={(e) => setFormData({ ...formData, hora: e.target.value })}
                className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 ${
                  errors.hora ? 'border-red-500' : 'border-gray-300'
                }`}
              />
              {errors.hora && <p className="text-red-500 text-xs mt-1">{errors.hora}</p>}
            </div>

            <div className="md:col-span-2">
              <label className="block text-sm font-medium text-gray-700 mb-2">Notas</label>
              <textarea
                value={formData.notas}
                onChange={(e) => setFormData({ ...formData, notas: e.target.value })}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                rows="2"
                placeholder="Observaciones adicionales (opcional)"
              />
            </div>

            <div className="md:col-span-2 flex gap-3">
              <button
                type="submit"
                disabled={loading}
                className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg font-medium transition disabled:opacity-50"
              >
                Guardar Cita
              </button>
              <button
                type="button"
                onClick={resetForm}
                className="bg-gray-500 hover:bg-gray-600 text-white px-6 py-2 rounded-lg font-medium transition"
              >
                Cancelar
              </button>
            </div>
          </form>
        </div>
      )}

      {/* Tabs */}
      <div className="flex gap-2 border-b">
        <button
          onClick={() => setActiveTab('mis-citas')}
          className={`px-6 py-3 font-medium transition ${
            activeTab === 'mis-citas'
              ? 'text-blue-600 border-b-2 border-blue-600'
              : 'text-gray-600 hover:text-gray-900'
          }`}
        >
          Mis Citas ({citas.length})
        </button>
        <button
          onClick={() => setActiveTab('solicitudes')}
          className={`px-6 py-3 font-medium transition ${
            activeTab === 'solicitudes'
              ? 'text-blue-600 border-b-2 border-blue-600'
              : 'text-gray-600 hover:text-gray-900'
          }`}
        >
          Solicitudes Pendientes ({solicitudes.length})
        </button>
      </div>

      {/* Búsqueda */}
      <input
        type="text"
        placeholder="Buscar por cliente..."
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        className="w-full px-4 py-2 border border-gray-300 rounded-lg"
      />

      {/* Mis Citas */}
      {activeTab === 'mis-citas' && (
        <div className="space-y-3">
          {filteredCitas.length === 0 ? (
            <p className="text-gray-500 text-center py-8">No hay citas confirmadas</p>
          ) : (
            filteredCitas.map(cita => (
              <div key={cita.id} className="bg-white rounded-lg shadow p-4 border-l-4 border-blue-500">
                <div className="flex justify-between items-start mb-3">
                  <div>
                    <h4 className="font-semibold text-lg">{cita.clienteNombre}</h4>
                    <p className="text-gray-600">{cita.servicioNombre}</p>
                  </div>
                  <span className={`px-3 py-1 rounded-full text-sm font-medium border ${getColorEstado(cita.estado)}`}>
                    {cita.estado}
                  </span>
                </div>
                <p className="text-sm text-gray-600 mb-2"> {formatearFechaHora(cita.fechaHora)}</p>
                {cita.notas && (
                  <p className="text-sm text-gray-600 mb-3"> {cita.notas}</p>
                )}
                {(cita.estado === 'PENDIENTE' || cita.estado === 'CONFIRMADA') && (
                  <button
                    onClick={() => cancelarCita(cita.id)}
                    disabled={loading}
                    className="text-red-600 hover:text-red-900 text-sm font-medium disabled:opacity-50"
                  >
                    Cancelar Cita
                  </button>
                )}
              </div>
            ))
          )}
        </div>
      )}

      {/* Solicitudes */}
      {activeTab === 'solicitudes' && (
        <div className="space-y-3">
          {filteredSolicitudes.length === 0 ? (
            <p className="text-gray-500 text-center py-8">No hay solicitudes pendientes</p>
          ) : (
            filteredSolicitudes.map(cita => (
              <div key={cita.id} className="bg-white rounded-lg shadow p-4 border-l-4 border-yellow-500">
                <div className="flex justify-between items-start mb-3">
                  <div>
                    <h4 className="font-semibold text-lg">{cita.clienteNombre}</h4>
                    <p className="text-gray-600">{cita.servicioNombre}</p>
                  </div>
                  <span className="bg-yellow-100 text-yellow-800 px-3 py-1 rounded-full text-sm font-medium">
                    PENDIENTE
                  </span>
                </div>
                <p className="text-sm text-gray-600 mb-3"> {formatearFechaHora(cita.fechaHora)}</p>
                {cita.notas && (
                  <p className="text-sm text-gray-600 mb-3">{cita.notas}</p>
                )}
                <div className="flex gap-3">
                  <button
                    onClick={() => confirmarSolicitud(cita.id)}
                    disabled={loading}
                    className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded text-sm font-medium disabled:opacity-50"
                  >
                    Confirmar
                  </button>
                  <button
                    onClick={() => rechazarSolicitud(cita.id)}
                    disabled={loading}
                    className="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded text-sm font-medium disabled:opacity-50"
                  >
                    Rechazar
                  </button>
                </div>
              </div>
            ))
          )}
        </div>
      )}
    </div>
  );
};

// ============================================
// COMPONENTE DE GESTIÓN DE SERVICIOS (UC-06)
// ============================================
const Servicios = ({ token }) => {
  const [servicios, setServicios] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [formData, setFormData] = useState({
    codigo: '',
    nombre: '',
    descripcion: '',
    precio: '',
    duracionMinutos: '',
    maxConcurrentes: ''
  });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');

   useEffect(() => {
  if (token) {
    fetchServicios();
  }
}, [token]);

  const fetchServicios = async () => {
    setLoading(true);
    try {
      const response = await fetch(`${API_URL}/servicios`, {
        headers: { 'Authorization': `Bearer ${token}` }
      });
      if (response.ok) {
        const data = await response.json();
        setServicios(data);
      }
    } catch (err) {
      console.error('Error al obtener servicios:', err);
    } finally {
      setLoading(false);
    }
  };

  const validateForm = () => {
    const newErrors = {};
    
    if (!formData.codigo.trim()) {
      newErrors.codigo = 'El código es requerido';
    } else if (formData.codigo.length < 3) {
      newErrors.codigo = 'El código debe tener al menos 3 caracteres';
    }

    if (!formData.nombre.trim()) {
      newErrors.nombre = 'El nombre es requerido';
    } else if (formData.nombre.length < 3) {
      newErrors.nombre = 'El nombre debe tener al menos 3 caracteres';
    }

    if (!formData.descripcion.trim()) {
      newErrors.descripcion = 'La descripción es requerida';
    }

    if (!formData.precio || formData.precio <= 0) {
      newErrors.precio = 'El precio debe ser mayor a 0';
    }

    if (!formData.duracionMinutos || formData.duracionMinutos <= 0) {
      newErrors.duracionMinutos = 'La duración debe ser mayor a 0';
    }

    if (!formData.maxConcurrentes || formData.maxConcurrentes <= 0) {
      newErrors.maxConcurrentes = 'El cupo debe ser mayor a 0';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) return;

    setLoading(true);
    const url = editingId 
      ? `${API_URL}/servicios/${editingId}`
      : `${API_URL}/servicios`;
    
    const method = editingId ? 'PUT' : 'POST';

    const payload = {
      codigo: formData.codigo.trim(),
      nombre: formData.nombre.trim(),
      descripcion: formData.descripcion.trim(),
      precio: parseFloat(formData.precio),
      duracion: parseInt(formData.duracionMinutos, 10),  
      cupoMaximo: parseInt(formData.maxConcurrentes, 10),
      activo: true
    };

    console.log('=== DEBUG PAYLOAD ===');
    console.log('FormData original:', formData);
    console.log('Payload procesado:', payload);
    console.log('duracion:', payload.duracion, 'tipo:', typeof payload.duracion);
    console.log('cupoMaximo:', payload.cupoMaximo, 'tipo:', typeof payload.cupoMaximo);

    // Validar que no haya NaN
    if (isNaN(payload.precio) || isNaN(payload.duracion) || isNaN(payload.cupoMaximo)) {
      console.error('ERROR: Valores numéricos inválidos');
      alert('Error: Verifique que todos los campos numéricos estén llenos correctamente');
      setLoading(false);
      return;
    }

    try {
      const response = await fetch(url, {
        method,
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(payload)
      });

      if (response.ok) {
        await fetchServicios();
        resetForm();
        alert(editingId ? 'Servicio actualizado exitosamente' : 'Servicio creado exitosamente');
      } else {
        const error = await response.json();
        alert(error.message || 'Error al guardar servicio');
      }
    } catch (err) {
      console.error('Error al guardar servicio:', err);
      alert('Error al guardar servicio. Intente nuevamente.');
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (servicio) => {
    setFormData({
      codigo: servicio.codigo,
      nombre: servicio.nombre,
      descripcion: servicio.descripcion,
      precio: servicio.precio,
      duracionMinutos: servicio.duracionMinutos,
      maxConcurrentes: servicio.maxConcurrentes
    });
    setEditingId(servicio.id);
    setShowForm(true);
    setErrors({});
  };

  const handleDelete = async (id) => {
    if (!window.confirm('¿Está seguro de desactivar este servicio?')) return;

    setLoading(true);
    try {
      const response = await fetch(`${API_URL}/servicios/${id}/desactivar`, {
        method: 'PATCH',
        headers: { 'Authorization': `Bearer ${token}` }
      });

      if (response.ok) {
        await fetchServicios();
        alert('Servicio desactivado exitosamente');
      } else {
        alert('Error al desactivar servicio');
      }
    } catch (err) {
      console.error('Error al eliminar servicio:', err);
      alert('Error al desactivar servicio');
    } finally {
      setLoading(false);
    }
  };

  const resetForm = () => {
    setFormData({ codigo: '', nombre: '', descripcion: '', precio: '', duracionMinutos: '', maxConcurrentes: '' });
    setEditingId(null);
    setShowForm(false);
    setErrors({});
  };

  const filteredServicios = servicios.filter(servicio =>
    servicio.nombre.toLowerCase().includes(searchTerm.toLowerCase()) ||
    servicio.codigo.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h2 className="text-2xl font-bold text-gray-800">Gestión de Servicios</h2>
          <p className="text-gray-600 text-sm">UC-06: Gestionar Servicios Ofrecidos</p>
        </div>
        <button
          onClick={() => setShowForm(!showForm)}
          className="bg-gradient-to-r from-green-600 to-teal-600 hover:from-green-700 hover:to-teal-700 text-white px-6 py-2 rounded-lg font-medium transition transform hover:scale-105"
        >
          {showForm ? 'Cancelar' : '+ Nuevo Servicio'}
        </button>
      </div>

      {showForm && (
        <div className="bg-white rounded-lg shadow-lg p-6 border-l-4 border-green-500">
          <h3 className="text-xl font-semibold mb-4 text-gray-800">
            {editingId ? 'Editar Servicio' : 'Nuevo Servicio'}
          </h3>
          <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Código *
              </label>
              <input
                type="text"
                value={formData.codigo}
                onChange={(e) => setFormData({...formData, codigo: e.target.value})}
                className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-green-500 ${
                  errors.codigo ? 'border-red-500' : 'border-gray-300'
                }`}
                placeholder="SRV001"
                disabled={editingId !== null}
              />
              {errors.codigo && (
                <p className="text-red-500 text-xs mt-1">{errors.codigo}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Nombre *
              </label>
              <input
                type="text"
                value={formData.nombre}
                onChange={(e) => setFormData({...formData, nombre: e.target.value})}
                className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-green-500 ${
                  errors.nombre ? 'border-red-500' : 'border-gray-300'
                }`}
                placeholder="Masaje Relajante"
              />
              {errors.nombre && (
                <p className="text-red-500 text-xs mt-1">{errors.nombre}</p>
              )}
            </div>

            <div className="md:col-span-2">
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Descripción *
              </label>
              <textarea
                value={formData.descripcion}
                onChange={(e) => setFormData({...formData, descripcion: e.target.value})}
                className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-green-500 ${
                  errors.descripcion ? 'border-red-500' : 'border-gray-300'
                }`}
                placeholder="Descripción detallada del servicio..."
                rows="3"
              />
              {errors.descripcion && (
                <p className="text-red-500 text-xs mt-1">{errors.descripcion}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Precio (Q) *
              </label>
              <input
                type="number"
                step="0.01"
                min="0.01"
                value={formData.precio}
                onChange={(e) => setFormData({...formData, precio: e.target.value})}
                className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-green-500 ${
                  errors.precio ? 'border-red-500' : 'border-gray-300'
                }`}
                placeholder="150.00"
                required
              />
              {errors.precio && (
                <p className="text-red-500 text-xs mt-1">{errors.precio}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Duración (minutos) *
              </label>
              <input
                type="number"
                min="1"
                step="1"
                value={formData.duracionMinutos}
                onChange={(e) => setFormData({...formData, duracionMinutos: e.target.value})}
                className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-green-500 ${
                  errors.duracionMinutos ? 'border-red-500' : 'border-gray-300'
                }`}
                placeholder="60"
                required
              />
              {errors.duracionMinutos && (
                <p className="text-red-500 text-xs mt-1">{errors.duracionMinutos}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Cupo Máximo *
              </label>
              <input
                type="number"
                min="1"
                max="50"
                step="1"
                value={formData.maxConcurrentes}
                onChange={(e) => setFormData({...formData, maxConcurrentes: e.target.value})}
                className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-green-500 ${
                  errors.maxConcurrentes ? 'border-red-500' : 'border-gray-300'
                }`}
                placeholder="5"
                required
              />
              {errors.maxConcurrentes && (
                <p className="text-red-500 text-xs mt-1">{errors.maxConcurrentes}</p>
              )}
              <p className="text-xs text-gray-500 mt-1">
                Clientes que pueden reservar en el mismo horario
              </p>
            </div>

            <div className="md:col-span-2 flex gap-3">
              <button
                type="submit"
                disabled={loading}
                className="bg-green-600 hover:bg-green-700 text-white px-6 py-2 rounded-lg font-medium transition flex items-center gap-2 disabled:opacity-50"
              >
                <Save size={18} />
                {editingId ? 'Actualizar' : 'Guardar'}
              </button>
              <button
                type="button"
                onClick={resetForm}
                className="bg-gray-500 hover:bg-gray-600 text-white px-6 py-2 rounded-lg font-medium transition"
              >
                Cancelar
              </button>
            </div>
          </form>
        </div>
      )}

      <div className="bg-white rounded-lg shadow-lg">
        <div className="p-4 border-b">
          <input
            type="text"
            placeholder="Buscar por nombre o código..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500"
          />
        </div>

        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50 border-b">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Código</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Nombre</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Precio</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Duración</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Cupo</th>
                <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase">Acciones</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200">
              {loading ? (
                <tr>
                  <td colSpan="6" className="px-6 py-8 text-center text-gray-500">
                    Cargando servicios...
                  </td>
                </tr>
              ) : filteredServicios.length === 0 ? (
                <tr>
                  <td colSpan="6" className="px-6 py-8 text-center text-gray-500">
                    No hay servicios registrados
                  </td>
                </tr>
              ) : (
                filteredServicios.map((servicio) => (
                  <tr key={servicio.id} className="hover:bg-gray-50 transition">
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{servicio.codigo}</td>
                    <td className="px-6 py-4 text-sm text-gray-900">
                      <div>
                        <div className="font-medium">{servicio.nombre}</div>
                        <div className="text-xs text-gray-500">{servicio.descripcion}</div>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600">Q {servicio.precio}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600">{servicio.duracionMinutos} min</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600">{servicio.maxConcurrentes}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium space-x-3">
                      <button
                        onClick={() => handleEdit(servicio)}
                        className="text-green-600 hover:text-green-900 inline-flex items-center gap-1"
                      >
                        <Edit2 size={16} />
                        Editar
                      </button>
                      <button
                        onClick={() => handleDelete(servicio.id)}
                        className="text-red-600 hover:text-red-900 inline-flex items-center gap-1"
                      >
                        <Trash2 size={16} />
                        Desactivar
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

// ============================================
// COMPONENTE DE GESTIÓN DE FACTURAS (UC-W05, UC-W08)
// ============================================
const Facturas = ({ token }) => {
  const [facturas, setFacturas] = useState([]);
  const [citas, setCitas] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    citaId: '',
    observaciones: ''
  });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
  if (token) {
    fetchFacturas();
    fetchCitasAtendidas();
  }
}, [token]);

  const fetchFacturas = async () => {
    setLoading(true);
    try {
      const response = await fetch(`${API_URL}/facturas`, {
        headers: { 'Authorization': `Bearer ${token}` }
      });
      if (response.ok) {
        const data = await response.json();
        setFacturas(data);
      }
    } catch (err) {
      console.error('Error al obtener facturas:', err);
    } finally {
      setLoading(false);
    }
  };

  const fetchCitasAtendidas = async () => {
    try {
      const response = await fetch(`${API_URL}/citas?estado=ATENDIDA`, {
        headers: { 'Authorization': `Bearer ${token}` }
      });
      if (response.ok) {
        const data = await response.json();
        // Filtrar solo citas que NO tienen factura
        const citasSinFactura = data.filter(cita => {
          return !facturas.some(factura => factura.citaId === cita.id);
        });
        setCitas(citasSinFactura);
      }
    } catch (err) {
      console.error('Error al obtener citas:', err);
    }
  };

  const validateForm = () => {
    const newErrors = {};
    
    if (!formData.citaId) {
      newErrors.citaId = 'Debe seleccionar una cita';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) return;

    setLoading(true);

    try {
      const response = await fetch(`${API_URL}/facturas`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
          citaId: parseInt(formData.citaId),
          observaciones: formData.observaciones.trim()
        })
      });

      if (response.ok) {
        await fetchFacturas();
        await fetchCitasAtendidas();
        resetForm();
        alert('Factura generada exitosamente');
      } else {
        const error = await response.json();
        alert(error.message || 'Error al generar factura');
      }
    } catch (err) {
      console.error('Error al generar factura:', err);
      alert('Error al generar factura. Intente nuevamente.');
    } finally {
      setLoading(false);
    }
  };

  const handleDescargar = (facturaId) => {
    // Simulación de descarga
    alert(`Descargando factura #${facturaId} (función en desarrollo)`);
    // En producción, esto generaría un PDF
  };

  const resetForm = () => {
    setFormData({ citaId: '', observaciones: '' });
    setShowForm(false);
    setErrors({});
  };

  const filteredFacturas = facturas.filter(factura =>
    factura.numeroFactura?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    factura.clienteNombre?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const formatFecha = (fechaStr) => {
    try {
      const fecha = new Date(fechaStr);
      return fecha.toLocaleDateString('es-GT', { 
        year: 'numeric', 
        month: 'long', 
        day: 'numeric' 
      });
    } catch {
      return fechaStr;
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h2 className="text-2xl font-bold text-gray-800">Gestión de Facturas</h2>
          <p className="text-gray-600 text-sm">UC-W05: Generar Factura</p>
        </div>
        <button
          onClick={() => setShowForm(!showForm)}
          className="bg-gradient-to-r from-green-600 to-teal-600 hover:from-green-700 hover:to-teal-700 text-white px-6 py-2 rounded-lg font-medium transition transform hover:scale-105"
        >
          {showForm ? 'Cancelar' : '+ Nueva Factura'}
        </button>
      </div>

      {showForm && (
        <div className="bg-white rounded-lg shadow-lg p-6 border-l-4 border-green-500">
          <h3 className="text-xl font-semibold mb-4 text-gray-800">
            Generar Nueva Factura
          </h3>
          <form onSubmit={handleSubmit} className="space-y-4">
            
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Cita a Facturar *
              </label>
              <select
                value={formData.citaId}
                onChange={(e) => setFormData({...formData, citaId: e.target.value})}
                className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-green-500 ${
                  errors.citaId ? 'border-red-500' : 'border-gray-300'
                }`}
              >
                <option value="">Seleccione una cita atendida</option>
                {citas.map((cita) => (
                  <option key={cita.id} value={cita.id}>
                    {cita.clienteNombre} - {cita.servicioNombre} - {formatFecha(cita.fechaHora)}
                  </option>
                ))}
              </select>
              {errors.citaId && (
                <p className="text-red-500 text-xs mt-1">{errors.citaId}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Observaciones (opcional)
              </label>
              <textarea
                value={formData.observaciones}
                onChange={(e) => setFormData({...formData, observaciones: e.target.value})}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500"
                placeholder="Notas adicionales..."
                rows="3"
              />
            </div>

            <div className="flex gap-3">
              <button
                type="submit"
                disabled={loading}
                className="bg-green-600 hover:bg-green-700 text-white px-6 py-2 rounded-lg font-medium transition flex items-center gap-2 disabled:opacity-50"
              >
                <FileText size={18} />
                Generar Factura
              </button>
              <button
                type="button"
                onClick={resetForm}
                className="bg-gray-500 hover:bg-gray-600 text-white px-6 py-2 rounded-lg font-medium transition"
              >
                Cancelar
              </button>
            </div>
          </form>
        </div>
      )}

      <div className="bg-white rounded-lg shadow-lg">
        <div className="p-4 border-b">
          <input
            type="text"
            placeholder="Buscar por número de factura o cliente..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500"
          />
        </div>

        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50 border-b">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Número</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Cliente</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Servicio</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Monto</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Fecha</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Estado</th>
                <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase">Acciones</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200">
              {loading ? (
                <tr>
                  <td colSpan="7" className="px-6 py-8 text-center text-gray-500">
                    Cargando facturas...
                  </td>
                </tr>
              ) : filteredFacturas.length === 0 ? (
                <tr>
                  <td colSpan="7" className="px-6 py-8 text-center text-gray-500">
                    No hay facturas registradas
                  </td>
                </tr>
              ) : (
                filteredFacturas.map((factura) => (
                  <tr key={factura.id} className="hover:bg-gray-50 transition">
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                      #{factura.numeroFactura}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                      {factura.clienteNombre || 'N/A'}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                      {factura.servicioNombre || 'N/A'}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-semibold text-green-600">
                      Q {factura.monto?.toFixed(2) || '0.00'}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                      {formatFecha(factura.fechaEmision)}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`px-2 py-1 text-xs font-semibold rounded-full ${
                        factura.estado === 'PAGADA' 
                          ? 'bg-green-100 text-green-800' 
                          : 'bg-yellow-100 text-yellow-800'
                      }`}>
                        {factura.estado}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                      <button
                        onClick={() => handleDescargar(factura.id)}
                        className="text-blue-600 hover:text-blue-900 inline-flex items-center gap-1"
                      >
                        <Eye size={16} />
                        Ver/Descargar
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

// ============================================
// COMPONENTE DE HISTORIAL DE SESIONES (UC-W04)
// ============================================
const HistorialSesiones = ({ token }) => {
  const [historial, setHistorial] = useState([]);
  const [clientes, setClientes] = useState([]);
  const [servicios, setServicios] = useState([]);
  const [loading, setLoading] = useState(false);
  const [filtros, setFiltros] = useState({
    clienteId: '',
    servicioId: '',
    estado: '',
    fechaInicio: '',
    fechaFin: ''
  });

    useEffect(() => {
  if (token) {
    fetchClientes();
    fetchServicios();
    fetchHistorial();
  }
}, [token]);

  const fetchClientes = async () => {
    try {
      const response = await fetch(`${API_URL}/clientes`, {
        headers: { 'Authorization': `Bearer ${token}` }
      });
      if (response.ok) {
        const data = await response.json();
        setClientes(data);
      }
    } catch (err) {
      console.error('Error al obtener clientes:', err);
    }
  };

  const fetchServicios = async () => {
    try {
      const response = await fetch(`${API_URL}/servicios`, {
        headers: { 'Authorization': `Bearer ${token}` }
      });
      if (response.ok) {
        const data = await response.json();
        setServicios(data);
      }
    } catch (err) {
      console.error('Error al obtener servicios:', err);
    }
  };

  const fetchHistorial = async () => {
    setLoading(true);
    try {
      let url = `${API_URL}/citas`;
      const params = new URLSearchParams();
      
      if (filtros.clienteId) params.append('clienteId', filtros.clienteId);
      if (filtros.servicioId) params.append('servicioId', filtros.servicioId);
      if (filtros.estado) params.append('estado', filtros.estado);
      if (filtros.fechaInicio) params.append('fechaInicio', filtros.fechaInicio);
      if (filtros.fechaFin) params.append('fechaFin', filtros.fechaFin);
      
      if (params.toString()) {
        url += `?${params.toString()}`;
      }

      const response = await fetch(url, {
        headers: { 'Authorization': `Bearer ${token}` }
      });

      if (response.ok) {
        const data = await response.json();
        setHistorial(data);
      }
    } catch (err) {
      console.error('Error al obtener historial:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleBuscar = (e) => {
    e.preventDefault();
    fetchHistorial();
  };

  const handleLimpiarFiltros = () => {
    setFiltros({
      clienteId: '',
      servicioId: '',
      estado: '',
      fechaInicio: '',
      fechaFin: ''
    });
    setTimeout(() => fetchHistorial(), 100);
  };

  const exportarCSV = () => {
    if (historial.length === 0) {
      alert('No hay datos para exportar');
      return;
    }

    const headers = ['ID', 'Cliente', 'Servicio', 'Fecha/Hora', 'Estado', 'Observaciones'];
    const rows = historial.map(cita => [
      cita.id,
      cita.clienteNombre,
      cita.servicioNombre,
      formatFechaHora(cita.fechaHora),
      cita.estado,
      cita.observaciones || ''
    ]);

    let csvContent = headers.join(',') + '\n';
    rows.forEach(row => {
      csvContent += row.map(cell => `"${cell}"`).join(',') + '\n';
    });

    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = `historial_sesiones_${new Date().toISOString().split('T')[0]}.csv`;
    link.click();
  };

  const formatFechaHora = (fechaStr) => {
    try {
      const fecha = new Date(fechaStr);
      return fecha.toLocaleString('es-GT', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      });
    } catch {
      return fechaStr;
    }
  };

  const getEstadoColor = (estado) => {
    switch (estado) {
      case 'CONFIRMADA':
        return 'bg-blue-100 text-blue-800';
      case 'PENDIENTE':
        return 'bg-yellow-100 text-yellow-800';
      case 'ATENDIDA':
        return 'bg-green-100 text-green-800';
      case 'CANCELADA':
      case 'RECHAZADA':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h2 className="text-2xl font-bold text-gray-800">Historial de Sesiones</h2>
          <p className="text-gray-600 text-sm">UC-W04: Consultar Historial de Sesiones</p>
        </div>
        <button
          onClick={exportarCSV}
          className="bg-gradient-to-r from-purple-600 to-indigo-600 hover:from-purple-700 hover:to-indigo-700 text-white px-6 py-2 rounded-lg font-medium transition transform hover:scale-105 flex items-center gap-2"
        >
          <BarChart3 size={18} />
          Exportar CSV
        </button>
      </div>

      {/* Filtros */}
      <div className="bg-white rounded-lg shadow-lg p-6">
        <h3 className="text-lg font-semibold mb-4 text-gray-800">Filtros de Búsqueda</h3>
        <form onSubmit={handleBuscar} className="grid grid-cols-1 md:grid-cols-3 gap-4">
          
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Cliente
            </label>
            <select
              value={filtros.clienteId}
              onChange={(e) => setFiltros({...filtros, clienteId: e.target.value})}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500"
            >
              <option value="">Todos los clientes</option>
              {clientes.map((cliente) => (
                <option key={cliente.id} value={cliente.id}>
                  {cliente.nombreCompleto}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Servicio
            </label>
            <select
              value={filtros.servicioId}
              onChange={(e) => setFiltros({...filtros, servicioId: e.target.value})}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500"
            >
              <option value="">Todos los servicios</option>
              {servicios.map((servicio) => (
                <option key={servicio.id} value={servicio.id}>
                  {servicio.nombre}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Estado
            </label>
            <select
              value={filtros.estado}
              onChange={(e) => setFiltros({...filtros, estado: e.target.value})}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500"
            >
              <option value="">Todos los estados</option>
              <option value="PENDIENTE">Pendiente</option>
              <option value="CONFIRMADA">Confirmada</option>
              <option value="ATENDIDA">Atendida</option>
              <option value="CANCELADA">Cancelada</option>
              <option value="RECHAZADA">Rechazada</option>
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Fecha Inicio
            </label>
            <input
              type="date"
              value={filtros.fechaInicio}
              onChange={(e) => setFiltros({...filtros, fechaInicio: e.target.value})}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Fecha Fin
            </label>
            <input
              type="date"
              value={filtros.fechaFin}
              onChange={(e) => setFiltros({...filtros, fechaFin: e.target.value})}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500"
            />
          </div>

          <div className="flex items-end gap-2">
            <button
              type="submit"
              className="flex-1 bg-purple-600 hover:bg-purple-700 text-white px-6 py-2 rounded-lg font-medium transition"
            >
              Buscar
            </button>
            <button
              type="button"
              onClick={handleLimpiarFiltros}
              className="px-4 py-2 bg-gray-500 hover:bg-gray-600 text-white rounded-lg font-medium transition"
            >
              Limpiar
            </button>
          </div>
        </form>
      </div>

      {/* Tabla de Historial */}
      <div className="bg-white rounded-lg shadow-lg overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50 border-b">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">ID</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Cliente</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Servicio</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Fecha/Hora</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Estado</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Observaciones</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200">
              {loading ? (
                <tr>
                  <td colSpan="6" className="px-6 py-8 text-center text-gray-500">
                    Cargando historial...
                  </td>
                </tr>
              ) : historial.length === 0 ? (
                <tr>
                  <td colSpan="6" className="px-6 py-8 text-center text-gray-500">
                    No se encontraron sesiones con los filtros aplicados
                  </td>
                </tr>
              ) : (
                historial.map((cita) => (
                  <tr key={cita.id} className="hover:bg-gray-50 transition">
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                      #{cita.id}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                      {cita.clienteNombre}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                      {cita.servicioNombre}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                      {formatFechaHora(cita.fechaHora)}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`px-2 py-1 text-xs font-semibold rounded-full ${getEstadoColor(cita.estado)}`}>
                        {cita.estado}
                      </span>
                    </td>
                    <td className="px-6 py-4 text-sm text-gray-600 max-w-xs truncate">
                      {cita.observaciones || '-'}
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>

        {/* Resumen */}
        {historial.length > 0 && (
          <div className="bg-gray-50 px-6 py-4 border-t">
            <p className="text-sm text-gray-600">
              <strong>Total de sesiones:</strong> {historial.length}
            </p>
          </div>
        )}
      </div>
    </div>
  );
};

// ============================================
// COMPONENTE DE REPORTES (UC-W06)
// ============================================
const Reportes = ({ token }) => {
  const [reportes, setReportes] = useState({
    totalCitas: 0,
    citasPendientes: 0,
    citasConfirmadas: 0,
    citasAtendidas: 0,
    citasCanceladas: 0,
    ingresosTotales: 0,
    serviciosMasSolicitados: [],
    clientesFrecuentes: []
  });
  const [loading, setLoading] = useState(false);
  const [filtros, setFiltros] = useState({
    fechaInicio: '',
    fechaFin: ''
  });

  useEffect(() => {
    // Establecer fechas por defecto (último mes)
    const hoy = new Date();
    const unMesAtras = new Date();
    unMesAtras.setMonth(hoy.getMonth() - 1);
    
    setFiltros({
      fechaInicio: unMesAtras.toISOString().split('T')[0],
      fechaFin: hoy.toISOString().split('T')[0]
    });
  }, []);

  useEffect(() => {
    if (filtros.fechaInicio && filtros.fechaFin) {
      fetchReportes();
    }
  }, [token, filtros]);

  const fetchReportes = async () => {
    setLoading(true);
    try {
      // Obtener citas
      const citasResponse = await fetch(
        `${API_URL}/citas?fechaInicio=${filtros.fechaInicio}&fechaFin=${filtros.fechaFin}`,
        { headers: { 'Authorization': `Bearer ${token}` } }
      );
      
      // Obtener facturas
      const facturasResponse = await fetch(`${API_URL}/facturas`, {
        headers: { 'Authorization': `Bearer ${token}` }
      });

      if (citasResponse.ok && facturasResponse.ok) {
        const citas = await citasResponse.json();
        const facturas = await facturasResponse.json();

        // Calcular métricas
        const totalCitas = citas.length;
        const citasPendientes = citas.filter(c => c.estado === 'PENDIENTE').length;
        const citasConfirmadas = citas.filter(c => c.estado === 'CONFIRMADA').length;
        const citasAtendidas = citas.filter(c => c.estado === 'ATENDIDA').length;
        const citasCanceladas = citas.filter(c => c.estado === 'CANCELADA' || c.estado === 'RECHAZADA').length;

        // Calcular ingresos
        const ingresosTotales = facturas
          .filter(f => {
            const fechaFactura = new Date(f.fechaEmision);
            return fechaFactura >= new Date(filtros.fechaInicio) && 
                   fechaFactura <= new Date(filtros.fechaFin);
          })
          .reduce((sum, f) => sum + (f.monto || 0), 0);

        // Servicios más solicitados
        const serviciosCont = {};
        citas.forEach(cita => {
          const key = cita.servicioNombre;
          serviciosCont[key] = (serviciosCont[key] || 0) + 1;
        });
        const serviciosMasSolicitados = Object.entries(serviciosCont)
          .map(([nombre, cantidad]) => ({ nombre, cantidad }))
          .sort((a, b) => b.cantidad - a.cantidad)
          .slice(0, 5);

        // Clientes frecuentes
        const clientesCont = {};
        citas.forEach(cita => {
          const key = cita.clienteNombre;
          clientesCont[key] = (clientesCont[key] || 0) + 1;
        });
        const clientesFrecuentes = Object.entries(clientesCont)
          .map(([nombre, cantidad]) => ({ nombre, cantidad }))
          .sort((a, b) => b.cantidad - a.cantidad)
          .slice(0, 5);

        setReportes({
          totalCitas,
          citasPendientes,
          citasConfirmadas,
          citasAtendidas,
          citasCanceladas,
          ingresosTotales,
          serviciosMasSolicitados,
          clientesFrecuentes
        });
      }
    } catch (err) {
      console.error('Error al generar reportes:', err);
    } finally {
      setLoading(false);
    }
  };

  const exportarPDF = () => {
    alert('Exportación a PDF en desarrollo. Por ahora, usa la función de imprimir del navegador.');
    window.print();
  };

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h2 className="text-2xl font-bold text-gray-800">Reportes Generales</h2>
          <p className="text-gray-600 text-sm">UC-W06: Generar Reporte General</p>
        </div>
        <button
          onClick={exportarPDF}
          className="bg-gradient-to-r from-red-600 to-pink-600 hover:from-red-700 hover:to-pink-700 text-white px-6 py-2 rounded-lg font-medium transition transform hover:scale-105 flex items-center gap-2"
        >
          <BarChart3 size={18} />
          Exportar PDF
        </button>
      </div>

      {/* Filtros de Fecha */}
      <div className="bg-white rounded-lg shadow-lg p-6">
        <h3 className="text-lg font-semibold mb-4 text-gray-800">Rango de Fechas</h3>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Fecha Inicio
            </label>
            <input
              type="date"
              value={filtros.fechaInicio}
              onChange={(e) => setFiltros({...filtros, fechaInicio: e.target.value})}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Fecha Fin
            </label>
            <input
              type="date"
              value={filtros.fechaFin}
              onChange={(e) => setFiltros({...filtros, fechaFin: e.target.value})}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500"
            />
          </div>
        </div>
      </div>

      {loading ? (
        <div className="text-center py-12">
          <p className="text-gray-600">Generando reportes...</p>
        </div>
      ) : (
        <>
          {/* Métricas Principales */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div className="bg-gradient-to-br from-blue-500 to-blue-600 rounded-lg shadow-lg p-6 text-white">
              <h3 className="text-lg font-semibold mb-2">Total de Sesiones</h3>
              <p className="text-4xl font-bold">{reportes.totalCitas}</p>
            </div>

            <div className="bg-gradient-to-br from-green-500 to-green-600 rounded-lg shadow-lg p-6 text-white">
              <h3 className="text-lg font-semibold mb-2">Ingresos Totales</h3>
              <p className="text-4xl font-bold">Q {reportes.ingresosTotales.toFixed(2)}</p>
            </div>

            <div className="bg-gradient-to-br from-purple-500 to-purple-600 rounded-lg shadow-lg p-6 text-white">
              <h3 className="text-lg font-semibold mb-2">Sesiones Atendidas</h3>
              <p className="text-4xl font-bold">{reportes.citasAtendidas}</p>
            </div>
          </div>

          {/* Estados de Citas */}
          <div className="bg-white rounded-lg shadow-lg p-6">
            <h3 className="text-xl font-semibold mb-4 text-gray-800">Estados de Sesiones</h3>
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
              <div className="text-center p-4 bg-yellow-50 rounded-lg">
                <p className="text-2xl font-bold text-yellow-600">{reportes.citasPendientes}</p>
                <p className="text-sm text-gray-600">Pendientes</p>
              </div>
              <div className="text-center p-4 bg-blue-50 rounded-lg">
                <p className="text-2xl font-bold text-blue-600">{reportes.citasConfirmadas}</p>
                <p className="text-sm text-gray-600">Confirmadas</p>
              </div>
              <div className="text-center p-4 bg-green-50 rounded-lg">
                <p className="text-2xl font-bold text-green-600">{reportes.citasAtendidas}</p>
                <p className="text-sm text-gray-600">Atendidas</p>
              </div>
              <div className="text-center p-4 bg-red-50 rounded-lg">
                <p className="text-2xl font-bold text-red-600">{reportes.citasCanceladas}</p>
                <p className="text-sm text-gray-600">Canceladas</p>
              </div>
            </div>
          </div>

          {/* Servicios y Clientes */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {/* Servicios Más Solicitados */}
            <div className="bg-white rounded-lg shadow-lg p-6">
              <h3 className="text-xl font-semibold mb-4 text-gray-800">Top 5 Servicios</h3>
              <div className="space-y-3">
                {reportes.serviciosMasSolicitados.map((servicio, index) => (
                  <div key={index} className="flex items-center justify-between">
                    <span className="text-gray-700">{servicio.nombre}</span>
                    <span className="bg-blue-100 text-blue-800 px-3 py-1 rounded-full text-sm font-semibold">
                      {servicio.cantidad}
                    </span>
                  </div>
                ))}
              </div>
            </div>

            {/* Clientes Frecuentes */}
            <div className="bg-white rounded-lg shadow-lg p-6">
              <h3 className="text-xl font-semibold mb-4 text-gray-800">Top 5 Clientes</h3>
              <div className="space-y-3">
                {reportes.clientesFrecuentes.map((cliente, index) => (
                  <div key={index} className="flex items-center justify-between">
                    <span className="text-gray-700">{cliente.nombre}</span>
                    <span className="bg-green-100 text-green-800 px-3 py-1 rounded-full text-sm font-semibold">
                      {cliente.cantidad} visitas
                    </span>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </>
      )}
    </div>
  );
};

// ============================================
// COMPONENTE PRINCIPAL APP
// ============================================
const App = () => {
  const [token, setToken] = useState(null);
  const [usuario, setUsuario] = useState('');
  const [currentView, setCurrentView] = useState('clientes');
  const [sidebarOpen, setSidebarOpen] = useState(true);

  // Carga token al iniciar 
  useEffect(() => {
    const savedToken = localStorage.getItem('token');
    const savedUsername = localStorage.getItem('username');
    
    if (savedToken && savedUsername) {
      console.log('Token encontrado en localStorage, restaurando sesión...');
      setToken(savedToken);
      setUsuario(savedUsername);
    }
  }, []);

  const handleLogin = (newToken, nombreUsuario) => {
    console.log('=== LOGIN DEBUG ===');
    console.log('Token recibido:', newToken);
    console.log('Usuario:', nombreUsuario);
    
    // ✅ GUARDAR EN localStorage PRIMERO
    localStorage.setItem('token', newToken);
    localStorage.setItem('username', nombreUsuario);
    
    console.log('Token guardado en localStorage');
    
    // LUEGO actualizar estado de React
    setToken(newToken);
    setUsuario(nombreUsuario);
  };

  const handleLogout = () => {
    if (window.confirm('¿Está seguro de cerrar sesión?')) {
      // Limpiar localStorage también
      localStorage.removeItem('token');
      localStorage.removeItem('username');
      
      setToken(null);
      setUsuario('');
      setCurrentView('clientes');
    }
  };

  if (!token) {
    return <Login onLogin={handleLogin} />;
  }

  const menuItems = [
    { id: 'clientes', label: 'Clientes', icon: Users },
    { id: 'servicios', label: 'Servicios', icon: Package},
    { id: 'citas', label: 'Citas', icon: Calendar },
    { id: 'facturas', label: 'Facturas', icon: FileText },
    { id: 'historial', label: 'Historial', icon: Clock }, 
    { id: 'reportes', label: 'Reportes', icon: BarChart3 }
  ];

  return (
    <div className="min-h-screen bg-gray-100 flex">
      <div className={`${sidebarOpen ? 'w-64' : 'w-20'} bg-gradient-to-b from-gray-900 to-gray-800 text-white transition-all duration-300 flex flex-col`}>
        <div className="p-4 border-b border-gray-700 flex items-center justify-between">
          {sidebarOpen && (
            <div>
              <h1 className="text-xl font-bold">Bienestar</h1>
              <p className="text-xs text-gray-400">Sistema de Gestión</p>
            </div>
          )}
          <button
            onClick={() => setSidebarOpen(!sidebarOpen)}
            className="p-2 hover:bg-gray-700 rounded-lg transition"
          >
            <Menu size={20} />
          </button>
        </div>

        <nav className="flex-1 p-4 space-y-2">
          {menuItems.map(item => {
            const Icon = item.icon;
            return (
              <button
                key={item.id}
                onClick={() => setCurrentView(item.id)}
                className={`w-full flex items-center gap-3 px-4 py-3 rounded-lg transition ${
                  currentView === item.id
                    ? 'bg-gradient-to-r from-blue-600 to-purple-600 text-white'
                    : 'text-gray-300 hover:bg-gray-700'
                }`}
              >
                <Icon size={20} />
                {sidebarOpen && <span className="font-medium">{item.label}</span>}
              </button>
            );
          })}
        </nav>

        <div className="p-4 border-t border-gray-700">
          <div className={`flex items-center gap-3 ${!sidebarOpen && 'justify-center'}`}>
            <div className="w-10 h-10 bg-gradient-to-r from-blue-500 to-purple-600 rounded-full flex items-center justify-center font-bold">
              {usuario.charAt(0).toUpperCase()}
            </div>
            {sidebarOpen && (
              <div className="flex-1 min-w-0">
                <p className="text-sm font-medium truncate">{usuario}</p>
                <p className="text-xs text-gray-400">Administrador</p>
              </div>
            )}
          </div>
          <button
            onClick={handleLogout}
            className="w-full mt-3 flex items-center justify-center gap-2 px-4 py-2 bg-red-600 hover:bg-red-700 rounded-lg transition"
          >
            <LogOut size={18} />
            {sidebarOpen && <span>Cerrar Sesión</span>}
          </button>
        </div>
      </div>

      <div className="flex-1 overflow-auto">
        <div className="p-8">
          {currentView === 'clientes' && <Clientes token={token} />}
          {currentView === 'servicios' && <Servicios token={token} />}
          {currentView === 'citas' && <Citas token={token} />}
          {currentView === 'facturas' && <Facturas token={token} />} 
          {currentView === 'historial' && <HistorialSesiones token={token} />}
          {currentView === 'reportes' && <Reportes token={token} />} 
        </div>
      </div>
    </div>
  );
};

export default App;