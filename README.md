# Sistema de Gestion de Inventarios - AndesTech S.A.S. (ISWZ1103)

**Asignatura:** ISWZ1103 - Programacion II  
**Universidad:** Universidad de Las Americas (UDLA) - Facultad de Ingenieria y Ciencias Aplicadas  
**Grupo:** Grupo 8  
**Fecha de Entrega:** 23/11/2026  
**Integrantes:**
- Lion Stadtmuller Caballero
- Juan Martin Sanchez Falconi
- Paula Simbana Mantuano

---

## Estructura de Paquetes

```text
ISWZ1103-GestionInventario-Grupo8/
├── src/
│   ├── modelo/
│   │   ├── InventarioException.java
│   │   ├── Producto.java
│   │   ├── ProductoPerecedero.java
│   │   └── ProductoNoPerecedero.java
│   ├── negocio/
│   │   ├── IGestorInventario.java
│   │   └── GestorInventario.java
│   └── interfaz/
│       ├── ConsolaInventario.java
│       └── Main.java
├── docs/
│   └── diagrama_clases_uml.drawio
├── .gitignore
└── README.md
```

## Compilacion y Ejecucion

```bash
javac -d bin src/modelo/*.java src/negocio/*.java src/interfaz/*.java
java -cp bin interfaz.Main
```
