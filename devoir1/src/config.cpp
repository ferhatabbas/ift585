#include "config.h"
#include <iostream>
#include <fstream>
#include <algorithm>

/**
   Default config key
 **/
const char *def_key[] = {
  "f_in",
  "f_out",
  "bit_error",
  "bit_error_rate",
  "code_corr",
  "reject",
  "trame_size",
  "trame_error",
  "selective_repeat",
  "transmission_limit",
  "station1_interval",
  "station2_interval",
  "window_size"};

/**
   Initialisation du vecteur de clé de configuration
 **/
const std::vector<Config::key> Config::_key(def_key, std::end(def_key));


/**
   Début de l'implémentation de classe Config
 **/

Config::Config(){
  this->Initialize();
}

Config::Config(std::string fname){
  this->_fname = fname;
  this->Initialize();
}


void
Config::Initialize(){
  for(auto k : this->_key){
    this->_config[k] = "0";
  }
}


void
Config::ReadFile(std::string fname){
  this->_fname = fname;
  this->ReadFile();
}
/**
   Lecture du fichier de configuration
 **/
void
Config::ReadFile(){
  std::ifstream f;
  std::string line;
  std::vector<std::string> el;
  f.open(this->_fname.c_str());
  if(f.is_open()){
    while(std::getline(f, line)){
      auto st = line.find('#');
      if(st != std::string::npos)
        line.erase(st, line.length()); // Delete comment in file
      line.erase(std::remove(line.begin(), line.end(), ' '),
                 line.end()); // Delete useless space in line
      line.erase(std::remove(line.begin(), line.end(), '"'),
                 line.end()); // Delete " in line default everithing is a string
      el = this->Split(line, ':'); // Split key:value
      if(el.size() < 2) // Check if value is empty
        continue;
      if(el[1].find('{') != std::string::npos)
        this->AppendDict(el[0], el[1]);
      else if(el[1].find('[') != std::string::npos)
        this->AppendList(el[0], el[1]);
      else
        this->AppendValue(el[0], el[1]);
      el.clear();
    }
  }
  f.close();
}


/**
   Fonction de debug
   TODO: supprimer pour le projet final
 **/
void
Config::PPrint(){
  for(auto it = this->_config.cbegin(); it != this->_config.cend(); ++it){
    std::cout << it->first << ": " << it->second << "\n";
  }
}

bool
Config::GetBool(Config::ConfVal cf){
	std::string v = this->_config.find(this->_key[cf])->second;
	return (v.find("True") != std::string::npos || v.find("true") != std::string::npos)? true : false;
}

void
Config::AppendValue(key k, value v){
  v.erase(std::remove(v.begin(), v.end(), ','), v.end());
  this->_config[k] = v;
}

void
Config::AppendList(key k, list l){
  l.erase(std::remove(l.begin(), l.end(), '['), l.end());
  l.erase(std::remove(l.begin(), l.end(), ']'), l.end());
  this->_config[k] = l;
}

void
Config::AppendDict(key k, dict d){
}

/**
   Fonction qui permet de splitter 
 **/
std::vector<std::string>
Config::Split(const std::string &s, char delim){
  std::vector<std::string> elems;
  std::stringstream ss(s);
  std::string item;
  while (std::getline(ss, item, delim)) {
    elems.push_back(item);
  }
  return elems;
}
