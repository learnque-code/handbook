module.exports = {
    optimize: process.env.NODE_ENV === "production" ? {
        entrypoints: [
            "js/stimulus-application.js",
        ],
        bundle: true,
        minify: true,
        target: "es2018",
    } : {},
    packageOptions: {
        knownEntrypoints: [
            "@hotwired/turbo",
            "stimulus",
            "tailwindcss"
        ]
    },
    plugins: [
        "@snowpack/plugin-postcss",
    ],
    exclude: [
        "*.config.js",
        "**/node_modules/**/*",
        "**/*.json",
        ".gitignore"
    ],
    buildOptions: {
        out: "../resources/static",
        clean: true,
    },
    devOptions: {
        out: "../resources/static",
        clean: true,
    }
}
