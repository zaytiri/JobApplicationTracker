
const { BrowserWindow, app, ipcMain, Notification } = require('electron');
var api = require('../libraries/start_api')
const path = require('path');

const isDev = !app.isPackaged;

function createWindow() {
  const win = new BrowserWindow({
    // width: 1500,
    // height: 900,
    backgroundColor: "white",
    webPreferences: {
      nodeIntegration: false,
      worldSafeExecuteJavaScript: true,
      contextIsolation: true,
      preload: path.join(__dirname, 'preload.js')
    },
    autoHideMenuBar: true
  })

  win.loadFile('src/app/main.html');
  win.maximize();
  // win.webContents.openDevTools()
}

if (isDev) {
  require('electron-reload')(__dirname, {
    electron: path.join(__dirname, '..', '..','node_modules', '.bin', 'electron')
  })
}

ipcMain.on('notify', (_, message) => {
  new Notification({title: 'Notifiation', body: message}).show();
})

app.whenReady().then(createWindow);

api.start();