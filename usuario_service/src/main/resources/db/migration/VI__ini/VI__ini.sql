CREATE TABLE usuario
    usuario_id BIGINT CONSTRAINT USUARIO_PK PRIMARY KEY;
    numrun INTEGER(8) NOT NULL;
    dvrun CHAR(1) NOT NULL;
    @Column(name = "dvrun", nullable = false, length = 1)
    private String dvRun;

    @Column(name = "appaterno", nullable = false, length = 25)
    private String apPaterno;
    @Column(name = "apmaterno", nullable = false, length = 25)
    private String apMaterno;
    @Column(nullable = false, length = 40)
    private String nombres;

    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    @Column(length = 100)
    private String direccion;
    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @Column(nullable = false, length = 12)
    private String telefono;
    @Column(nullable = false, unique = true, length = 50)
    private String correo;

    @Column(nullable = false, length = 25)
    private String profesion;
    @ManyToOne
    @JoinColumn(name = "sucursal_id")
    private Sucursal sucursal;