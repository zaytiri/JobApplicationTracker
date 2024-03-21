How to set up Electron + React

install node.js

input command ``node -v`` and ``npm -v`` to make sure version are displayed meaning it's all installed correctly

install following packages:
npm install --save electron react react-dom
npm install --save-dev @babel/core @babel/preset-env @babel/preset-react css-loader style-loader sass-loader babel-loader webpack webpack-cli electron-reload

to run application:
use ``npm run watch`` to compile react code in one terminal
use ``npm start`` to start electron app in a second terminal

