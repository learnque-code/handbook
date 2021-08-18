module.exports = {
    plugins: [
        require("tailwindcss")("./tailwind.config.js"),
        require("autoprefixer"),
        require("cssnano")(),
        require("postcss-flexbugs-fixes"),
        require("postcss-preset-env")({
            autoprefixer: {
                flexbox: "no-2009",
            },
            stage: 3,
        }),
        ...process.env.NODE_ENV === "production"
            ? [

            ]
            : []
    ],
};
