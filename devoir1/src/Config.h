#pragma once

/*
namespace Config
{
	int transmissionMediaLimit = 10;
	int station1TimerInterval = 100;
	int station2TimerInterval = 100;
	
	const int MAX_PKT = 2;
	bool selectiveRepeat = false;
}
*/
#include <string>
#include <map>
#include <vector>
#include <sstream>

#pragma once

/**
   Classe Config elle permet de charger le fichier de configuration
   et de récuperer les valeurs du fichier grâce à l'enum CONFV
 **/
class Config{
public:
  //type helper
  typedef std::string key;
  typedef std::string value;
  typedef std::string list;
  typedef std::string dict;
private:
  typedef std::map<key, value> mConf;
  
  std::string _fname;
  mConf _config;
  static const std::vector<key> _key;
  
public:
  enum ConfVal{
    FIN,      // f_in
    FOUT,     // f_out
    BERR,     // bit_error
    BERATE,   // bit_error_rate
    CERR,     // code_corr
    REJECT,   // reject
    TSIZE,    // trame_size
    TERR,     // tame_error
    SREP,     // selective_repeat
    TLIMIT,   // transmission_limit
    TST1,     // Timer station1 interval
    TST2,     // Timer station2 interval
    WSIZE     // window_size
  };
  static const int MAX_PKT = 2;
  
  Config();
  Config(std::string);
  ~Config(){}

  std::vector<int> ReadProp();

  void ReadFile();
  void ReadFile(std::string);
  void PPrint();

  template<typename V>
      V GetProp(ConfVal cf){
    std::stringstream ss;
    V res;
    mConf::const_iterator it = this->_config.find(this->_key[cf]);
    ss << it->second;
    ss >> res;
    return res;
  }

	  bool GetBool(ConfVal);

  template<typename V>
      std::vector<V> GetListProp(ConfVal cf){
    mConf::const_iterator it = this->_config.find(this->_key[cf]);
    std::string line = it->second;
    std::vector<V> res;
    for(auto el : this->Split(line, ',')){
      V conv;
      std::stringstream ss(el);
      ss >> conv;
      res.push_back(conv);
    }
    return res;
  }
  
private:
  void Initialize();
  void AppendValue(key, value);
  void AppendList(key, list);
  void AppendDict(key, dict);
  
  std::vector<std::string> Split(const std::string &, char);
};
