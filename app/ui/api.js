const { spawn } = require('child_process');
const path = require('path');

function start() {
    var baseCommand = 'java';
    var args = ['-jar', 'api.jar'];
    const options = {
      cwd: path.join(__dirname, 'build'),
    };
    const subprocess = spawn(baseCommand, args, options);
}

module.exports = {
  start
};