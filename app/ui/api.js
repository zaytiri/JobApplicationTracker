const { spawn } = require('child_process');

function start() {
    var baseCommand = 'java';
    var args = ['-jar', 'api.jar'];
    const options = {
      cwd: '<path_to_jar_file>',
    };
    const subprocess = spawn(baseCommand, args, options);
}

module.exports = {
  start
};