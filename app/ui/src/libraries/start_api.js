const { spawn } = require('child_process');
const path = require('path');

function start() {
    var baseCommand = 'java';
    var args = ['-jar', 'api.jar'];
    const options = {
      cwd: path.join(__dirname, '..', '..', 'external'),
    };
    const subprocess = spawn(baseCommand, args, options);

    subprocess.on('error', (err) => {
      console.error('Error occurred while spawning process:', err);
    });
}

module.exports = {
  start
};